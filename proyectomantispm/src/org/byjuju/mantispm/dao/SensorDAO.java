package org.byjuju.mantispm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.byjuju.mantispm.modelo.Sensor;

/**
 * DAO para la tabla sensor
 */
public class SensorDAO implements GenericDAO<Sensor> {

    @Override
    public void insertar(Sensor s) throws Exception {
        String sql = "INSERT INTO sensor (uid_sensor, nombre, tipo_sensor_id, equipo_id, instalado_en, referencia) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getUidSensor());
            ps.setString(2, s.getNombre());
            ps.setInt(3, s.getTipoSensorId());
            ps.setInt(4, s.getEquipoId());
            if (s.getInstaladoEn() != null) ps.setTimestamp(5, s.getInstaladoEn());
            else ps.setTimestamp(5, null);
            ps.setString(6, s.getReferencia());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setSensorId(rs.getInt(1));
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            // Duplicate uid_sensor: idempotencia a nivel applicacion
            System.err.println("Sensor con mismo UID ya existe: " + s.getUidSensor());
            throw ex;
        }
    }

    @Override
    public void actualizar(Sensor s) throws Exception {
        String sql = "UPDATE sensor SET nombre=?, tipo_sensor_id=?, equipo_id=?, instalado_en=?, referencia=? WHERE sensor_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setInt(2, s.getTipoSensorId());
            ps.setInt(3, s.getEquipoId());
            ps.setTimestamp(4, s.getInstaladoEn());
            ps.setString(5, s.getReferencia());
            ps.setInt(6, s.getSensorId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM sensor WHERE sensor_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Sensor obtenerPorId(int id) throws Exception {
        String sql = "SELECT sensor_id, uid_sensor, nombre, tipo_sensor_id, equipo_id, instalado_en, referencia FROM sensor WHERE sensor_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sensor s = new Sensor();
                    s.setSensorId(rs.getInt("sensor_id"));
                    s.setUidSensor(rs.getString("uid_sensor"));
                    s.setNombre(rs.getString("nombre"));
                    s.setTipoSensorId(rs.getInt("tipo_sensor_id"));
                    s.setEquipoId(rs.getInt("equipo_id"));
                    s.setInstaladoEn(rs.getTimestamp("instalado_en"));
                    s.setReferencia(rs.getString("referencia"));
                    return s;
                }
            }
        }
        return null;
    }

    @Override
    public List<Sensor> listarTodos() throws Exception {
        String sql = "SELECT sensor_id, uid_sensor, nombre, tipo_sensor_id, equipo_id, instalado_en, referencia FROM sensor";
        List<Sensor> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sensor s = new Sensor();
                s.setSensorId(rs.getInt("sensor_id"));
                s.setUidSensor(rs.getString("uid_sensor"));
                s.setNombre(rs.getString("nombre"));
                s.setTipoSensorId(rs.getInt("tipo_sensor_id"));
                s.setEquipoId(rs.getInt("equipo_id"));
                s.setInstaladoEn(rs.getTimestamp("instalado_en"));
                s.setReferencia(rs.getString("referencia"));
                lista.add(s);
            }
        }
        return lista;
    }

    /**
     * Helper: obtener sensor por uid
     */
    public Sensor obtenerPorUid(String uid) throws Exception {
        String sql = "SELECT sensor_id, uid_sensor, nombre, tipo_sensor_id, equipo_id, instalado_en, referencia FROM sensor WHERE uid_sensor=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sensor s = new Sensor();
                    s.setSensorId(rs.getInt("sensor_id"));
                    s.setUidSensor(rs.getString("uid_sensor"));
                    s.setNombre(rs.getString("nombre"));
                    s.setTipoSensorId(rs.getInt("tipo_sensor_id"));
                    s.setEquipoId(rs.getInt("equipo_id"));
                    s.setInstaladoEn(rs.getTimestamp("instalado_en"));
                    s.setReferencia(rs.getString("referencia"));
                    return s;
                }
            }
        }
        return null;
    }
}
