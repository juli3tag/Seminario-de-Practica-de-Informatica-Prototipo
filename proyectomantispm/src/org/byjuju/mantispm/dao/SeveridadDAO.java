package org.byjuju.mantispm.dao;

import org.byjuju.mantispm.modelo.Severidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla severidad_alerta
 */
public class SeveridadDAO {

    /**
     * Lista todas las severidades (severidad_id, codigo, descripcion)
     */
    public List<Severidad> listarTodos() throws Exception {
        String sql = "SELECT severidad_id, codigo, descripcion FROM severidad_alerta ORDER BY severidad_id";
        List<Severidad> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Severidad s = new Severidad();
                s.setSeveridadId(rs.getInt("severidad_id"));
                s.setCodigo(rs.getString("codigo"));
                s.setDescripcion(rs.getString("descripcion"));
                lista.add(s);
            }
        }
        return lista;
    }

    /**
     * Obtener severidad por id
     */
    public Severidad obtenerPorId(int id) throws Exception {
        String sql = "SELECT severidad_id, codigo, descripcion FROM severidad_alerta WHERE severidad_id = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Severidad s = new Severidad();
                    s.setSeveridadId(rs.getInt("severidad_id"));
                    s.setCodigo(rs.getString("codigo"));
                    s.setDescripcion(rs.getString("descripcion"));
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Obtener severidad por código (ej: "ALTA")
     */
    public Severidad obtenerPorCodigo(String codigo) throws Exception {
        String sql = "SELECT severidad_id, codigo, descripcion FROM severidad_alerta WHERE codigo = ? LIMIT 1";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Severidad s = new Severidad();
                    s.setSeveridadId(rs.getInt("severidad_id"));
                    s.setCodigo(rs.getString("codigo"));
                    s.setDescripcion(rs.getString("descripcion"));
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Lista solo los códigos (útil para menús o validaciones rápidas)
     */
    public List<String> listarCodigos() throws Exception {
        String sql = "SELECT codigo FROM severidad_alerta ORDER BY severidad_id";
        List<String> res = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(rs.getString("codigo"));
            }
        }
        return res;
    }
}
