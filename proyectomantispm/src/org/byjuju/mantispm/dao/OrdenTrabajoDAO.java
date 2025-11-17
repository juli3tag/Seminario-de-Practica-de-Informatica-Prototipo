package org.byjuju.mantispm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.byjuju.mantispm.modelo.OrdenTrabajo;

/**
 * DAO para 'orden_trabajo'
 */
public class OrdenTrabajoDAO implements GenericDAO<OrdenTrabajo> {

    @Override
    public void insertar(OrdenTrabajo o) throws Exception {
        String sql = "INSERT INTO orden_trabajo (alerta_id, equipo_id, creado_por, prioridad_id, estado_id, descripcion, creado_en, asignado_a, cerrado_en) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (o.getAlertaId() != null) ps.setLong(1, o.getAlertaId());
            else ps.setNull(1, Types.BIGINT);
            ps.setInt(2, o.getEquipoId());
            if (o.getCreadoPor() != null) ps.setInt(3, o.getCreadoPor());
            else ps.setNull(3, Types.INTEGER);
            if (o.getPrioridadId() != null) ps.setInt(4, o.getPrioridadId());
            else ps.setNull(4, Types.INTEGER);
            if (o.getEstadoId() != null) ps.setInt(5, o.getEstadoId());
            else ps.setNull(5, Types.INTEGER);
            ps.setString(6, o.getDescripcion());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            if (o.getAsignadoA() != null) ps.setInt(8, o.getAsignadoA());
            else ps.setNull(8, Types.INTEGER);
            if (o.getCerradoEn() != null) ps.setTimestamp(9, o.getCerradoEn());
            else ps.setNull(9, Types.TIMESTAMP);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) o.setOrdenId(rs.getInt(1));
            }
        }
    }

    @Override
    public void actualizar(OrdenTrabajo o) throws Exception {
        String sql = "UPDATE orden_trabajo SET estado_id=?, descripcion=?, asignado_a=?, cerrado_en=? WHERE orden_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (o.getEstadoId() != null) ps.setInt(1, o.getEstadoId());
            else ps.setNull(1, Types.INTEGER);
            ps.setString(2, o.getDescripcion());
            if (o.getAsignadoA() != null) ps.setInt(3, o.getAsignadoA());
            else ps.setNull(3, Types.INTEGER);
            if (o.getCerradoEn() != null) ps.setTimestamp(4, o.getCerradoEn());
            else ps.setNull(4, Types.TIMESTAMP);
            ps.setInt(5, o.getOrdenId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM orden_trabajo WHERE orden_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public OrdenTrabajo obtenerPorId(int id) throws Exception {
        String sql = "SELECT orden_id, alerta_id, equipo_id, creado_por, prioridad_id, estado_id, descripcion, creado_en, asignado_a, cerrado_en FROM orden_trabajo WHERE orden_id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrdenTrabajo o = new OrdenTrabajo();
                    o.setOrdenId(rs.getInt("orden_id"));
                    long alerta = rs.getLong("alerta_id");
                    if (!rs.wasNull()) o.setAlertaId(alerta);
                    o.setEquipoId(rs.getInt("equipo_id"));
                    int cp = rs.getInt("creado_por");
                    if (!rs.wasNull()) o.setCreadoPor(cp);
                    int pr = rs.getInt("prioridad_id");
                    if (!rs.wasNull()) o.setPrioridadId(pr);
                    int es = rs.getInt("estado_id");
                    if (!rs.wasNull()) o.setEstadoId(es);
                    o.setDescripcion(rs.getString("descripcion"));
                    o.setCreadoEn(rs.getTimestamp("creado_en"));
                    int as = rs.getInt("asignado_a");
                    if (!rs.wasNull()) o.setAsignadoA(as);
                    o.setCerradoEn(rs.getTimestamp("cerrado_en"));
                    return o;
                }
            }
        }
        return null;
    }

    @Override
    public List<OrdenTrabajo> listarTodos() throws Exception {
        String sql = "SELECT orden_id, alerta_id, equipo_id, creado_por, prioridad_id, estado_id, descripcion, creado_en, asignado_a, cerrado_en FROM orden_trabajo ORDER BY creado_en DESC LIMIT 100";
        List<OrdenTrabajo> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrdenTrabajo o = new OrdenTrabajo();
                o.setOrdenId(rs.getInt("orden_id"));
                long alerta = rs.getLong("alerta_id");
                if (!rs.wasNull()) o.setAlertaId(alerta);
                o.setEquipoId(rs.getInt("equipo_id"));
                int cp = rs.getInt("creado_por");
                if (!rs.wasNull()) o.setCreadoPor(cp);
                int pr = rs.getInt("prioridad_id");
                if (!rs.wasNull()) o.setPrioridadId(pr);
                int es = rs.getInt("estado_id");
                if (!rs.wasNull()) o.setEstadoId(es);
                o.setDescripcion(rs.getString("descripcion"));
                o.setCreadoEn(rs.getTimestamp("creado_en"));
                int as = rs.getInt("asignado_a");
                if (!rs.wasNull()) o.setAsignadoA(as);
                o.setCerradoEn(rs.getTimestamp("cerrado_en"));
                lista.add(o);
            }
        }
        return lista;
    }
}
