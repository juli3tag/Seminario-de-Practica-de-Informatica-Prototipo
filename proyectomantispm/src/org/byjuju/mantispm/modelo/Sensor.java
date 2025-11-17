package org.byjuju.mantispm.modelo;

import java.sql.Timestamp;

/**
 * POJO Sensor — mapea a la tabla 'sensor'
 */
public class Sensor {
    private int sensorId;
    private String uidSensor;
    private String nombre;
    private int tipoSensorId;
    private int equipoId;
    private Timestamp instaladoEn;
    private String referencia;

    public Sensor() {}

    public Sensor(int sensorId, String uidSensor, String nombre, int tipoSensorId, int equipoId) {
        this.sensorId = sensorId;
        this.uidSensor = uidSensor;
        this.nombre = nombre;
        this.tipoSensorId = tipoSensorId;
        this.equipoId = equipoId;
    }

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public String getUidSensor() { return uidSensor; }
    public void setUidSensor(String uidSensor) { this.uidSensor = uidSensor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getTipoSensorId() { return tipoSensorId; }
    public void setTipoSensorId(int tipoSensorId) { this.tipoSensorId = tipoSensorId; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public Timestamp getInstaladoEn() { return instaladoEn; }
    public void setInstaladoEn(Timestamp instaladoEn) { this.instaladoEn = instaladoEn; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId=" + sensorId +
                ", uidSensor='" + uidSensor + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoSensorId=" + tipoSensorId +
                ", equipoId=" + equipoId +
                '}';
    }
}
