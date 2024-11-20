package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {

    private  final StringProperty idCliente;
    private final StringProperty nombre;
    private final StringProperty telefono;
    private final StringProperty direccion;

    public Cliente(String idCliente, String nombre, String telefono, String direccion) {
        this.idCliente = new SimpleStringProperty(idCliente);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.direccion = new SimpleStringProperty(direccion);
    }

    public String getIdCliente() {
        return idCliente.get();
    }

    public StringProperty idClienteProperty() {
        return idCliente;
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public String getDireccion() {
        return direccion.get();
    }

    public StringProperty direccionProperty() {
        return direccion;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente.set(idCliente);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    @Override
    public String toString() {
        return idCliente.get() + ", " + nombre.get() + ", " + telefono.get() + ", " + direccion.get();
    }

}
