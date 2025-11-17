package org.byjuju.mantispm.modelo;

import java.sql.Timestamp;

/**
 * POJO OrdenTrabajo — mapea a la tabla 'orden_trabajo'
 */
public class OrdenTrabajo {
    private int ordenId;
    private Long alertaId;
    private int equipoId;
    private Integer creadoPor;
    private Integer prioridadId;
    private Integer estadoId;
    private String descripcion;
    private Timestamp creadoEn;
    private Integer asignadoA;
    private Timestamp cerradoEn;

    public OrdenTrabajo() {}

    public OrdenTrabajo(int ordenId, Long alertaId, int equipoId, Integer prioridadId, Integer estadoId, String descripcion) {
        this.ordenId = ordenId;
        this.alertaId = alertaId;
        this.equipoId = equipoId;
        this.prioridadId = prioridadId;
        this.estadoId = estadoId;
        this.descripcion = descripcion;
    }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public Long getAlertaId() { return alertaId; }
    public void setAlertaId(Long alertaId) { this.alertaId = alertaId; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }

    public Integer getPrioridadId() { return prioridadId; }
    public void setPrioridadId(Integer prioridadId) { this.prioridadId = prioridadId; }

    public Integer getEstadoId() { return estadoId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Timestamp getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Timestamp creadoEn) { this.creadoEn = creadoEn; }

    public Integer getAsignadoA() { return asignadoA; }
    public void setAsignadoA(Integer asignadoA) { this.asignadoA = asignadoA; }

    public Timestamp getCerradoEn() { return cerradoEn; }
    public void setCerradoEn(Timestamp cerradoEn) { this.cerradoEn = cerradoEn; }

    @Override
    public String toString() {
        return "OrdenTrabajo{" +
                "ordenId=" + ordenId +
                ", alertaId=" + alertaId +
                ", equipoId=" + equipoId +
                ", prioridadId=" + prioridadId +
                '}';
    }
}
