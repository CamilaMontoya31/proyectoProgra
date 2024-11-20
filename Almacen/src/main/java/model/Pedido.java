package model;

import java.util.Date;
import java.util.List;



public class Pedido {

    private String idPedido;
    private String idCliente;
    private Date fecha;
    private List<DetallePedido> detalles;
    private float total;

    public Pedido(String idPedido, String idCliente, Date fecha, List<DetallePedido> detalles,float total) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.detalles = detalles;
        this.total = total;
    }

    private double calcularTotal() {
        return detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        StringBuilder detallesString = new StringBuilder();
        for (DetallePedido detalle : detalles) {
            detallesString.append("|").append(detalle.toString());
        }
        return idPedido + "," + idCliente + "," + fecha + "," + total + detallesString;
    }
}
