package org.byjuju.mantispm.servicio;

import java.sql.Timestamp;

import org.byjuju.mantispm.dao.LecturaDAO;
import org.byjuju.mantispm.modelo.Lectura;

/**
 * Servicio encargado de la lógica relacionada con lecturas.
 * - Valida formato
 * - Invoca LecturaDAO.insertar
 * - Maneja idempotencia (re-lanzamiento de excepción para la capa superior)
 */
public class ServicioMantenimiento {

    private final LecturaDAO lecturaDAO;

    public ServicioMantenimiento(LecturaDAO lecturaDAO) {
        this.lecturaDAO = lecturaDAO;
    }

    /**
     * Registra una lectura. Maneja idempotencia a través de lectura_uid UNIQUE en BD.
     */

    public void registrarLectura(String lecturaUid, int sensorId, double valor, String unidad, Timestamp registradoEn, String payloadRaw) throws Exception {
        // Validaciones basicas
        if (lecturaUid == null || lecturaUid.trim().isEmpty()) {
            throw new IllegalArgumentException("lecturaUid es obligatorio");
        }
        if (sensorId <= 0) {
            throw new IllegalArgumentException("sensorId inválido");
        }

        Lectura l = new Lectura();
        l.setLecturaUid(lecturaUid);
        l.setSensorId(sensorId);
        l.setValor(valor);
        l.setUnidad(unidad);
        l.setRegistradoEn(registradoEn != null ? registradoEn : new Timestamp(System.currentTimeMillis()));
        l.setProcesada(false);
        l.setPayloadRaw(payloadRaw);

        try {
            lecturaDAO.insertar(l);
            System.out.println("Lectura insertada con id: " + l.getLecturaId());
        } catch (Exception ex) {
            // Si es duplicado (SQLIntegrityConstraintViolationException), re-lanzar con contexto
            throw ex;
        }
    }
}
