package org.byjuju.mantispm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.byjuju.mantispm.modelo.Alerta;

/**
 * DAO para tabla alerta
 */
public class AlertaDAO implements GenericDAO<Alerta> {

    @Override
    public void insertar(Alerta a) throws Exception {
        String sql = "INSERT INTO alerta (lectura_id, severidad_id, mensaje, creado_en, creado_por, procesado) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.getLecturaId() != null) ps.setLong(1, a.getLecturaId());
            else ps.setNull(1, Types.BIGINT);
            if (a.getSeveridadId() != null) ps.setInt(2, a.getSeveridadId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, a.getMensaje());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            if (a.getCreadoPor() != null) ps.setInt(5, a.getCreadoPor());
            else ps.setNull(5, Types.INTEGER);
            ps.setBoolean(6, a.isProcesado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setAlertaId(rs.getLong(1));
            }
        }
    }

    @Override
    public void actualizar(Alerta a) throws Exception {
        String sql = "UPDATE alerta SET mensaje=?, procesado=? WHERE alerta_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getMensaje());
            ps.setBoolean(2, a.isProcesado());
            ps.setLong(3, a.getAlertaId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM alerta WHERE alerta_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Alerta obtenerPorId(int id) throws Exception {
        String sql = "SELECT alerta_id, lectura_id, severidad_id, mensaje, creado_en, creado_por, procesado FROM alerta WHERE alerta_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Alerta a = new Alerta();
                    a.setAlertaId(rs.getLong("alerta_id"));
                    long lecturaId = rs.getLong("lectura_id");
                    if (!rs.wasNull()) a.setLecturaId(lecturaId);
                    a.setSeveridadId(rs.getInt("severidad_id"));
                    if (rs.wasNull()) a.setSeveridadId(null);
                    a.setMensaje(rs.getString("mensaje"));
                    a.setCreadoEn(rs.getTimestamp("creado_en"));
                    int creadoPor = rs.getInt("creado_por");
                    if (!rs.wasNull()) a.setCreadoPor(creadoPor);
                    a.setProcesado(rs.getBoolean("procesado"));
                    return a;
                }
            }
        }
        return null;
    }

    @Override
    public List<Alerta> listarTodos() throws Exception {
        String sql = "SELECT alerta_id, lectura_id, severidad_id, mensaje, creado_en, creado_por, procesado FROM alerta ORDER BY creado_en DESC LIMIT 100";
        List<Alerta> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Alerta a = new Alerta();
                a.setAlertaId(rs.getLong("alerta_id"));
                long lecturaId = rs.getLong("lectura_id");
                if (!rs.wasNull()) a.setLecturaId(lecturaId);
                int sev = rs.getInt("severidad_id");
                if (!rs.wasNull()) a.setSeveridadId(sev);
                a.setMensaje(rs.getString("mensaje"));
                a.setCreadoEn(rs.getTimestamp("creado_en"));
                int cp = rs.getInt("creado_por");
                if (!rs.wasNull()) a.setCreadoPor(cp);
                a.setProcesado(rs.getBoolean("procesado"));
                lista.add(a);
            }
        }
        return lista;
    }
}
