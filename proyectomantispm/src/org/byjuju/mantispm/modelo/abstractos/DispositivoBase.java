package org.byjuju.mantispm.modelo.abstractos;

/**
 * Clase abstracta que representa un dispositivo base (conceptual).
 * Sirve para mostrar uso de herencia en el modelo.
 */
public abstract class DispositivoBase {
    protected int id;
    protected String nombre;
    protected String ubicacion;

    public DispositivoBase(int id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }

    public abstract String mostrarEstado();
}
