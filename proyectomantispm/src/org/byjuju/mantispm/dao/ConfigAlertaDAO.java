package org.byjuju.mantispm.dao;

import org.byjuju.mantispm.modelo.ConfigAlerta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para config_alerta: contiene reglas por alcance
 */
public class ConfigAlertaDAO {

    /**
     * Obtiene reglas activas (lista)
     */
    public List<ConfigAlerta> listarActivas() throws Exception {
        String sql = "SELECT config_id, empresa_id, planta_id, equipo_id, tipo_sensor_id, umbral_alto, umbral_bajo, ventana_segundos, auto_crear_ot, prioridad_default_id, activo FROM config_alerta WHERE activo = TRUE";
        List<ConfigAlerta> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConfigAlerta c = mapConfig(rs);
                lista.add(c);
            }
        }
        return lista;
    }

    /**
     * Obtener regla aplicable, buscando en orden de especificidad:
     * 1) equipo + tipo_sensor
     * 2) planta + tipo_sensor
     * 3) empresa + tipo_sensor
     * 4) tipo_sensor global
     */
    public ConfigAlerta obtenerReglaAplicable(Integer empresaId, Integer plantaId, Integer equipoId, Integer tipoSensorId) throws Exception {
        try (Connection conn = ConexionBD.conectar()) {

            // 1) regla por equipo + tipo_sensor
            if (equipoId != null && tipoSensorId != null) {
                String q1 = "SELECT * FROM config_alerta WHERE equipo_id = ? AND tipo_sensor_id = ? AND activo = TRUE LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(q1)) {
                    ps.setInt(1, equipoId);
                    ps.setInt(2, tipoSensorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return mapConfig(rs);
                    }
                }
            }

            // 2) regla por planta + tipo_sensor
            if (plantaId != null && tipoSensorId != null) {
                String q2 = "SELECT * FROM config_alerta WHERE planta_id = ? AND tipo_sensor_id = ? AND activo = TRUE LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(q2)) {
                    ps.setInt(1, plantaId);
                    ps.setInt(2, tipoSensorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return mapConfig(rs);
                    }
                }
            }

            // 3) regla por empresa + tipo_sensor
            if (empresaId != null && tipoSensorId != null) {
                String q3 = "SELECT * FROM config_alerta WHERE empresa_id = ? AND tipo_sensor_id = ? AND activo = TRUE LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(q3)) {
                    ps.setInt(1, empresaId);
                    ps.setInt(2, tipoSensorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return mapConfig(rs);
                    }
                }
            }

            // 4) regla global por tipo_sensor
            if (tipoSensorId != null) {
                String q4 = "SELECT * FROM config_alerta WHERE tipo_sensor_id = ? AND activo = TRUE LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(q4)) {
                    ps.setInt(1, tipoSensorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return mapConfig(rs);
                    }
                }
            }
        }
        return null;
    }

    private ConfigAlerta mapConfig(ResultSet rs) throws SQLException {
        ConfigAlerta c = new ConfigAlerta();
        c.setConfigId(rs.getInt("config_id"));
        int emp = rs.getInt("empresa_id");
        if (!rs.wasNull()) c.setEmpresaId(emp);
        int pla = rs.getInt("planta_id");
        if (!rs.wasNull()) c.setPlantaId(pla);
        int eq = rs.getInt("equipo_id");
        if (!rs.wasNull()) c.setEquipoId(eq);
        int ts = rs.getInt("tipo_sensor_id");
        if (!rs.wasNull()) c.setTipoSensorId(ts);
        double ua = rs.getDouble("umbral_alto");
        if (!rs.wasNull()) c.setUmbralAlto(ua);
        double ub = rs.getDouble("umbral_bajo");
        if (!rs.wasNull()) c.setUmbralBajo(ub);
        int ven = rs.getInt("ventana_segundos");
        if (!rs.wasNull()) c.setVentanaSegundos(ven);
        boolean auto = rs.getBoolean("auto_crear_ot");
        c.setAutoCrearOt(auto);
        int pr = rs.getInt("prioridad_default_id");
        if (!rs.wasNull()) c.setPrioridadDefaultId(pr);
        boolean act = rs.getBoolean("activo");
        c.setActivo(act);
        return c;
    }
}
