package org.byjuju.mantispm.dao;

import java.util.List;

/**
 * Interfaz generica para DAOs
 */
public interface GenericDAO<T> {
    void insertar(T obj) throws Exception;
    void actualizar(T obj) throws Exception;
    void eliminar(int id) throws Exception;
    T obtenerPorId(int id) throws Exception;
    java.util.List<T> listarTodos() throws Exception;
}
