package model;

public class Articulo {
    private String idArticulo;
    private String descripcion;
    private double precio;
    private int cantidad; // Nueva propiedad

    public Articulo(String idArticulo, String descripcion, double precio, int cantidad) {
        this.idArticulo = idArticulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return idArticulo + ", " + descripcion + ", " + precio + ", " + cantidad;
    }
}
