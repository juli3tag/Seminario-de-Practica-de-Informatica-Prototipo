package org.byjuju.mantispm.modelo;

/**
 * POJO ConfigAlerta — reglas y parámetros para generar alertas
 */
public class ConfigAlerta {
    private int configId;
    private Integer empresaId;
    private Integer plantaId;
    private Integer equipoId;
    private Integer tipoSensorId;
    private Double umbralAlto;
    private Double umbralBajo;
    private Integer ventanaSegundos;
    private Boolean autoCrearOt;
    private Integer prioridadDefaultId;
    private Boolean activo;

    public ConfigAlerta() {}

    public int getConfigId() { return configId; }
    public void setConfigId(int configId) { this.configId = configId; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public Integer getPlantaId() { return plantaId; }
    public void setPlantaId(Integer plantaId) { this.plantaId = plantaId; }

    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }

    public Integer getTipoSensorId() { return tipoSensorId; }
    public void setTipoSensorId(Integer tipoSensorId) { this.tipoSensorId = tipoSensorId; }

    public Double getUmbralAlto() { return umbralAlto; }
    public void setUmbralAlto(Double umbralAlto) { this.umbralAlto = umbralAlto; }

    public Double getUmbralBajo() { return umbralBajo; }
    public void setUmbralBajo(Double umbralBajo) { this.umbralBajo = umbralBajo; }

    public Integer getVentanaSegundos() { return ventanaSegundos; }
    public void setVentanaSegundos(Integer ventanaSegundos) { this.ventanaSegundos = ventanaSegundos; }

    public Boolean getAutoCrearOt() { return autoCrearOt; }
    public void setAutoCrearOt(Boolean autoCrearOt) { this.autoCrearOt = autoCrearOt; }

    public Integer getPrioridadDefaultId() { return prioridadDefaultId; }
    public void setPrioridadDefaultId(Integer prioridadDefaultId) { this.prioridadDefaultId = prioridadDefaultId; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "ConfigAlerta{" +
                "configId=" + configId +
                ", tipoSensorId=" + tipoSensorId +
                ", umbralAlto=" + umbralAlto +
                ", autoCrearOt=" + autoCrearOt +
                '}';
    }
}
