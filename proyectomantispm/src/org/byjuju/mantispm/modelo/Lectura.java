package org.byjuju.mantispm.modelo;

import java.sql.Timestamp;

/**
 * POJO Lectura — mapea a la tabla 'lectura'
 */
public class Lectura {
    private long lecturaId;
    private String lecturaUid;
    private int sensorId;
    private double valor;
    private String unidad;
    private Timestamp registradoEn;
    private Timestamp ingestionTs;
    private boolean procesada;
    private String payloadRaw;

    public Lectura() {}

    public Lectura(long lecturaId, String lecturaUid, int sensorId, double valor, String unidad, Timestamp registradoEn, boolean procesada, String payloadRaw) {
        this.lecturaId = lecturaId;
        this.lecturaUid = lecturaUid;
        this.sensorId = sensorId;
        this.valor = valor;
        this.unidad = unidad;
        this.registradoEn = registradoEn;
        this.procesada = procesada;
        this.payloadRaw = payloadRaw;
    }

    public long getLecturaId() { return lecturaId; }
    public void setLecturaId(long lecturaId) { this.lecturaId = lecturaId; }

    public String getLecturaUid() { return lecturaUid; }
    public void setLecturaUid(String lecturaUid) { this.lecturaUid = lecturaUid; }

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public Timestamp getRegistradoEn() { return registradoEn; }
    public void setRegistradoEn(Timestamp registradoEn) { this.registradoEn = registradoEn; }

    public Timestamp getIngestionTs() { return ingestionTs; }
    public void setIngestionTs(Timestamp ingestionTs) { this.ingestionTs = ingestionTs; }

    public boolean isProcesada() { return procesada; }
    public void setProcesada(boolean procesada) { this.procesada = procesada; }

    public String getPayloadRaw() { return payloadRaw; }
    public void setPayloadRaw(String payloadRaw) { this.payloadRaw = payloadRaw; }

    @Override
    public String toString() {
        return "Lectura{" +
                "lecturaId=" + lecturaId +
                ", lecturaUid='" + lecturaUid + '\'' +
                ", sensorId=" + sensorId +
                ", valor=" + valor +
                ", registradoEn=" + registradoEn +
                '}';
    }
}
