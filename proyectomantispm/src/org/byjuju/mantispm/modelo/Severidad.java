package org.byjuju.mantispm.modelo;

/**
 * POJO Severidad
 */
public class Severidad {
    private int severidadId;
    private String codigo;
    private String descripcion;

    public Severidad() {}

    public Severidad(int severidadId, String codigo, String descripcion) {
        this.severidadId = severidadId;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getSeveridadId() {
        return severidadId;
    }

    public void setSeveridadId(int severidadId) {
        this.severidadId = severidadId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Severidad{" +
                "severidadId=" + severidadId +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
