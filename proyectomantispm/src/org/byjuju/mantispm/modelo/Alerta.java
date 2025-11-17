package org.byjuju.mantispm.modelo;

import java.sql.Timestamp;

/**
 * POJO Alerta — mapea a la tabla 'alerta'
 */
public class Alerta {
    private long alertaId;
    private Long lecturaId;
    private Integer severidadId;
    private String mensaje;
    private Timestamp creadoEn;
    private Integer creadoPor;
    private boolean procesado;

    public Alerta() {}

    public Alerta(long alertaId, Long lecturaId, Integer severidadId, String mensaje, Timestamp creadoEn, Integer creadoPor) {
        this.alertaId = alertaId;
        this.lecturaId = lecturaId;
        this.severidadId = severidadId;
        this.mensaje = mensaje;
        this.creadoEn = creadoEn;
        this.creadoPor = creadoPor;
    }

    public long getAlertaId() { return alertaId; }
    public void setAlertaId(long alertaId) { this.alertaId = alertaId; }

    public Long getLecturaId() { return lecturaId; }
    public void setLecturaId(Long lecturaId) { this.lecturaId = lecturaId; }

    public Integer getSeveridadId() { return severidadId; }
    public void setSeveridadId(Integer severidadId) { this.severidadId = severidadId; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Timestamp getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Timestamp creadoEn) { this.creadoEn = creadoEn; }

    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }

    public boolean isProcesado() { return procesado; }
    public void setProcesado(boolean procesado) { this.procesado = procesado; }

    @Override
    public String toString() {
        return "Alerta{" +
                "alertaId=" + alertaId +
                ", lecturaId=" + lecturaId +
                ", severidadId=" + severidadId +
                ", mensaje='" + mensaje + ''' +
                '}';
    }
}
