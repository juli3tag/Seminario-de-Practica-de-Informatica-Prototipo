package org.byjuju.mantispm.dao;

import org.byjuju.mantispm.modelo.Lectura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla lectura (series temporales)
 */
public class LecturaDAO implements GenericDAO<Lectura> {

    @Override
    public void insertar(Lectura l) throws Exception {
        String sql = "INSERT INTO lectura (lectura_uid, sensor_id, valor, unidad, registrado_en, ingestion_ts, procesada, payload_raw) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getLecturaUid());
            ps.setInt(2, l.getSensorId());
            ps.setDouble(3, l.getValor());
            ps.setString(4, l.getUnidad());
            ps.setTimestamp(5, l.getRegistradoEn());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setBoolean(7, l.isProcesada());
            if (l.getPayloadRaw() != null) ps.setString(8, l.getPayloadRaw());
            else ps.setNull(8, Types.VARCHAR);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) l.setLecturaId(rs.getLong(1));
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            // lectura_uid duplicada -> idempotencia
            System.err.println("Lectura duplicada: lectura_uid=" + l.getLecturaUid());
            throw ex;
        }
    }

    @Override
    public void actualizar(Lectura l) throws Exception {
        String sql = "UPDATE lectura SET valor=?, unidad=?, procesada=? WHERE lectura_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, l.getValor());
            ps.setString(2, l.getUnidad());
            ps.setBoolean(3, l.isProcesada());
            ps.setLong(4, l.getLecturaId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM lectura WHERE lectura_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Lectura obtenerPorId(int id) throws Exception {
        String sql = "SELECT lectura_id, lectura_uid, sensor_id, valor, unidad, registrado_en, ingestion_ts, procesada, payload_raw FROM lectura WHERE lectura_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lectura l = new Lectura();
                    l.setLecturaId(rs.getLong("lectura_id"));
                    l.setLecturaUid(rs.getString("lectura_uid"));
                    l.setSensorId(rs.getInt("sensor_id"));
                    l.setValor(rs.getDouble("valor"));
                    l.setUnidad(rs.getString("unidad"));
                    l.setRegistradoEn(rs.getTimestamp("registrado_en"));
                    l.setIngestionTs(rs.getTimestamp("ingestion_ts"));
                    l.setProcesada(rs.getBoolean("procesada"));
                    l.setPayloadRaw(rs.getString("payload_raw"));
                    return l;
                }
            }
        }
        return null;
    }

    @Override
    public List<Lectura> listarTodos() throws Exception {
        String sql = "SELECT lectura_id, lectura_uid, sensor_id, valor, unidad, registrado_en, ingestion_ts, procesada, payload_raw FROM lectura ORDER BY registrado_en DESC LIMIT 1000";
        List<Lectura> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Lectura l = new Lectura();
                l.setLecturaId(rs.getLong("lectura_id"));
                l.setLecturaUid(rs.getString("lectura_uid"));
                l.setSensorId(rs.getInt("sensor_id"));
                l.setValor(rs.getDouble("valor"));
                l.setUnidad(rs.getString("unidad"));
                l.setRegistradoEn(rs.getTimestamp("registrado_en"));
                l.setIngestionTs(rs.getTimestamp("ingestion_ts"));
                l.setProcesada(rs.getBoolean("procesada"));
                l.setPayloadRaw(rs.getString("payload_raw"));
                lista.add(l);
            }
        }
        return lista;
    }

    /**
     * Obtiene lecturas no procesadas
     */
    public List<Lectura> obtenerNoProcesadas(int limite) throws Exception {
        String sql = "SELECT lectura_id, lectura_uid, sensor_id, valor, unidad, registrado_en, ingestion_ts, procesada, payload_raw FROM lectura WHERE procesada = FALSE ORDER BY registrado_en LIMIT ?";
        List<Lectura> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lectura l = new Lectura();
                    l.setLecturaId(rs.getLong("lectura_id"));
                    l.setLecturaUid(rs.getString("lectura_uid"));
                    l.setSensorId(rs.getInt("sensor_id"));
                    l.setValor(rs.getDouble("valor"));
                    l.setUnidad(rs.getString("unidad"));
                    l.setRegistradoEn(rs.getTimestamp("registrado_en"));
                    l.setIngestionTs(rs.getTimestamp("ingestion_ts"));
                    l.setProcesada(rs.getBoolean("procesada"));
                    l.setPayloadRaw(rs.getString("payload_raw"));
                    lista.add(l);
                }
            }
        }
        return lista;
    }

    /**
     * Marca lectura como procesada
     */
    public void marcarProcesada(long lecturaId) throws Exception {
        String sql = "UPDATE lectura SET procesada = TRUE WHERE lectura_id = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, lecturaId);
            ps.executeUpdate();
        }
    }
}
