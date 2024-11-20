package model;

public class DetallePedido {
    private String idArticulo;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetallePedido(String idArticulo, int cantidad, double precioUnitario) {
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Getters y Setters
    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return idArticulo + "*" + cantidad + "*" + precioUnitario;
    }
}

