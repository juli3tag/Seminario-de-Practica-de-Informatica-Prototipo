package org.byjuju.mantispm.servicio;

import org.byjuju.mantispm.dao.*;
import org.byjuju.mantispm.modelo.ConfigAlerta;
import org.byjuju.mantispm.modelo.Lectura;
import org.byjuju.mantispm.modelo.Sensor;
import org.byjuju.mantispm.modelo.Severidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

/**
 * ProcesadorAlertas: batch que procesa lecturas no procesadas y genera alertas / OT.
 *
 * Esta versión:
 * - Obtiene el sensor real para determinar tipo_sensor y equipo.
 * - Busca la regla aplicable con ids reales.
 * - Crea alerta + OT dentro de una transacción JDBC.
 */
public class ProcesadorAlertas {

    private final LecturaDAO lecturaDAO;
    private final ConfigAlertaDAO configDAO;
    private final AlertaDAO alertaDAO;
    private final OrdenTrabajoDAO ordenDAO;
    private final SensorDAO sensorDAO;
    private final SeveridadDAO severidadDAO;

    public ProcesadorAlertas(LecturaDAO lecturaDAO, ConfigAlertaDAO configDAO, AlertaDAO alertaDAO, OrdenTrabajoDAO ordenDAO) {
        this.lecturaDAO = lecturaDAO;
        this.configDAO = configDAO;
        this.alertaDAO = alertaDAO;
        this.ordenDAO = ordenDAO;
        this.sensorDAO = new SensorDAO();
        this.severidadDAO = new SeveridadDAO();
    }

    /**
     * Batch principal que obtiene lecturas no procesadas y las procesa.
     */
    public void procesarLecturasNoProcesadas() throws Exception {
        List<Lectura> lecturas = lecturaDAO.obtenerNoProcesadas(100);
        System.out.println("Procesador: lecturas a procesar = " + lecturas.size());
        for (Lectura l : lecturas) {
            try {
                procesarUnaLectura(l);
            } catch (Exception ex) {
                System.err.println("Error procesando lectura " + l.getLecturaId() + " -> " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    protected void procesarUnaLectura(Lectura l) throws Exception {
        // 1) obtener sensor y sus ids de jerarquía
        Sensor s = null;
        try {
            s = sensorDAO.obtenerPorId(l.getSensorId());
        } catch (Exception ex) {
            System.err.println("No se pudo obtener sensor for lectura " + l.getLecturaId() + " -> " + ex.getMessage());
            ex.printStackTrace();
        }
        if (s == null) {
            // marcar procesada para evitar loops
            System.err.println("Sensor no encontrado para lectura " + l.getLecturaId() + ". Marcando como procesada para evitar loops.");
            lecturaDAO.marcarProcesada(l.getLecturaId());
            return;
        }

        Integer tipoSensorId = s.getTipoSensorId();
        Integer equipoId = s.getEquipoId();
        Integer plantaId = null;
        Integer empresaId = null;

        // 2) obtener la regla aplicable usando los ids reales
        ConfigAlerta regla = configDAO.obtenerReglaAplicable(empresaId, plantaId, equipoId, tipoSensorId);

        if (regla == null) {
            System.out.println("No existe regla aplicable para lectura " + l.getLecturaId() + " (tipoSensorId=" + tipoSensorId + "). Marcando como procesada.");
            lecturaDAO.marcarProcesada(l.getLecturaId());
            return;
        }

        // 3) Evaluar la lectura contra la regla
        boolean cumple = false;
        if (regla.getUmbralAlto() != null) {
            cumple = l.getValor() > regla.getUmbralAlto();
        } else if (regla.getUmbralBajo() != null) {
            cumple = l.getValor() < regla.getUmbralBajo();
        }

        if (cumple) {
            // crear alerta y OT dentro de una transacción JDBC para atomicidad
            crearAlertaYPosibleOtTransaccional(l, regla, equipoId);
        } else {
            lecturaDAO.marcarProcesada(l.getLecturaId());
        }
    }

    /**
     * Versión transaccional de creación de alerta + OT + marcas de lectura.
     * Ahora recibe equipoId explícito (proviene del sensor).
     */
    private void crearAlertaYPosibleOtTransaccional(Lectura l, ConfigAlerta regla, Integer equipoId) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);

            // obtener severidad según regla / lógica simple
            Integer severidadId = null;
            try {
                // ejemplo: si la regla tiene umbral alto, usamos 'ALTA' por defecto
                Severidad sev = severidadDAO.obtenerPorCodigo("ALTA");
                if (sev != null) severidadId = sev.getSeveridadId();
            } catch (Exception ex) {
                System.err.println("No se pudo obtener severidad desde BD: " + ex.getMessage());
            }

            // insertar alerta
            String sqlInsertAlerta = "INSERT INTO alerta (lectura_id, severidad_id, mensaje, creado_en, creado_por, procesado) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertAlerta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, l.getLecturaId());
                if (severidadId != null) ps.setInt(2, severidadId); else ps.setNull(2, Types.INTEGER);
                ps.setString(3, "Valor excede umbral: " + l.getValor());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setNull(5, Types.INTEGER);
                ps.setBoolean(6, false);
                ps.executeUpdate();

                long alertaId = -1;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) alertaId = rs.getLong(1);
                }

                System.out.println("Alerta creada id=" + alertaId);

                if (Boolean.TRUE.equals(regla.getAutoCrearOt())) {
                    String sqlInsertOt = "INSERT INTO orden_trabajo (alerta_id, equipo_id, creado_por, prioridad_id, estado_id, descripcion, creado_en) VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlInsertOt, Statement.RETURN_GENERATED_KEYS)) {
                        ps2.setLong(1, alertaId);
                        if (equipoId != null) ps2.setInt(2, equipoId);
                        else ps2.setInt(2, 1);
                        ps2.setNull(3, Types.INTEGER);
                        if (regla.getPrioridadDefaultId() != null) ps2.setInt(4, regla.getPrioridadDefaultId());
                        else ps2.setNull(4, Types.INTEGER);
                        ps2.setNull(5, Types.INTEGER);
                        ps2.setString(6, "OT generada automaticamente por alerta " + alertaId);
                        ps2.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                        ps2.executeUpdate();
                        try (ResultSet rs2 = ps2.getGeneratedKeys()) {
                            if (rs2.next()) System.out.println("Orden de trabajo creada id=" + rs2.getInt(1));
                        }
                    }
                }

                // marcar lectura como procesada
                String sqlMarcar = "UPDATE lectura SET procesada = TRUE WHERE lectura_id = ?";
                try (PreparedStatement ps3 = conn.prepareStatement(sqlMarcar)) {
                    ps3.setLong(1, l.getLecturaId());
                    ps3.executeUpdate();
                }
            }

            conn.commit();
        } catch (Exception ex) {
            if (conn != null) try { conn.rollback(); } catch (Exception e) { /* log */ }
            System.err.println("Error creando alerta/OT transaccional: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (Exception e) { /* ignore */ }
        }
    }
}
