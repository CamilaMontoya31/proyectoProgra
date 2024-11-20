package data;

import model.Pedido;
import model.DetallePedido;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PedidoData {
    private PrintWriter printWriter;
    private static final String FILE_NAME = "PEDIDOS.DAT";
    private String rutaArchivo;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private Scanner scanner;

    public PedidoData(String rutaArchivo) throws IOException {
        this.rutaArchivo = rutaArchivo;
        printWriter = new PrintWriter(new FileWriter(rutaArchivo,true)); //append = abrir para agregar
        scanner = new Scanner(new FileReader(rutaArchivo));
    }
    public void insertar(Pedido pedidos){
        printWriter.print(pedidos.getIdPedido()+";");
        printWriter.print(pedidos.getIdCliente()+";");
        printWriter.print(pedidos.getFecha()+";");
        printWriter.println(pedidos.getTotal()+";"); //hay un cambio de linea para escribir otro registro
        printWriter.flush();
    }
    public void closePrinterWriter(){
        printWriter.close();
    }
    public void closeScanner(){
        scanner.close();
    }

    public static List<Pedido> cargarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                String idPedido = partes[0];
                String idCliente = partes[1];
                Date fecha = DATE_FORMAT.parse(partes[2]);
                float total = Float.parseFloat(partes[3].split("\\|")[0]);

                List<DetallePedido> detalles = new ArrayList<>();
                String[] detallesPartes = partes[3].split("\\|");
                for (int i = 1; i < detallesPartes.length; i++) {
                    String[] detalle = detallesPartes[i].split("\\*");
                    String idArticulo = detalle[0];
                    int cantidad = Integer.parseInt(detalle[1]);
                    double precioUnitario = Double.parseDouble(detalle[2]);
                    detalles.add(new DetallePedido(idArticulo, cantidad, precioUnitario));
                }

                pedidos.add(new Pedido(idPedido, idCliente, fecha, detalles,total));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public static void guardarPedido(Pedido pedido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(pedido.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void agregarPedido(String idCliente, List<DetallePedido> detalles) {
        // String idPedido = IdClientes.obtenerConsecutivoPedido();
        Date fecha = new Date(); // Fecha actual
        // Pedido pedido = new Pedido(idPedido, idCliente, fecha, detalles);
        // guardarPedido(pedido);
    }

    /** ***************************************************************************
     * Verifica si un artículo está asociado a algún pedido.
     Guiselle ocupa este método **************************************************
     */
    public static boolean estaAsociadoAVenta(String idArticulo) {
        List<Pedido> pedidos = cargarPedidos();
        for (Pedido pedido : pedidos) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getIdArticulo().equals(idArticulo)) {
                    return true; // El artículo está asociado a un pedido
                }
            }
        }
        return false; // El artículo no está asociado a ningún pedido
    }
}
