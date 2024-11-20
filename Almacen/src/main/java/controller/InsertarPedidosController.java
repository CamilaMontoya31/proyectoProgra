package controller;

import data.ClienteData;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import data.PedidoData;
import model.Cliente;
import model.Pedido;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class InsertarPedidosController {
    private VBox layout; //permite agregar de arriba a abajo
    private String rutaArchivoTienda;
    private TextField tfnumPedido;
    private TextField tfnumCliente;
    private DatePicker tfFecha;
    private TextField tfTotalPorCobrar;

    public InsertarPedidosController(String rutaArchivoTienda) {
        this.rutaArchivoTienda = rutaArchivoTienda;
        createUI();
    }

    public void createUI(){
        layout = new VBox(10);
        layout.setPadding(new Insets(15));

        //label para el titulo
        Label lblTitulo = new Label("Insertar un nuevo pedido");

        Label lblnumPedido = new Label("Numero de Pedido: ");
        tfnumPedido = new TextField();
        Label lblnumCliente = new Label("Numero de Cliente: ");
        tfnumCliente = new TextField();
        Label lblFecha = new Label("Fecha: ");
        tfFecha = new DatePicker();
        Label lblTotalPorCobrar = new Label("Total por cobrar: ");
        tfTotalPorCobrar = new TextField();

        GridPane pane = new GridPane();
        pane.setHgap(5.5);
        pane.setVgap(5.5);//separación entre componente
        pane.add(lblnumPedido, 0,0);
        pane.add(tfnumPedido, 1,0);
        pane.add(lblnumCliente, 0,1);
        pane.add(tfnumCliente, 1,1);
        pane.add(lblFecha, 0,2);
        pane.add(tfFecha, 1,2);
        pane.add(lblTotalPorCobrar, 0,3);
        pane.add(tfTotalPorCobrar, 1,3);


        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> handleBtnInsertar());
        Button btnCancelar = new Button("Cancelar");
       btnCancelar.setOnAction(e ->handleBtnCancelar());
        pane.add(btnGuardar,0,4);
        pane.add(btnCancelar,1,4);
        this.layout.getChildren().addAll(lblTitulo,pane);


    }
    public void handleBtnInsertar (){//TODO: VALIDACIÓN SI ESTÁ VACIA
        String idPedido =  this.tfnumPedido.getText() ;
        String idCliente = tfnumCliente.getText();

        LocalDate selectedDate = tfFecha.getValue();
        ZonedDateTime zonedDateTime = selectedDate.atStartOfDay(ZoneId.of("America/Costa_Rica"));
        Date fecha = Date.from(zonedDateTime.toInstant());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        // Crear un SimpleDateFormat con el patrón deseado y la zona horaria de Costa Rica
        sdf.setTimeZone(TimeZone.getTimeZone("America/Costa_Rica"));

        // Formatear la fecha según la zona horaria de Costa Rica
        String fechaFormateada = sdf.format(fecha);

        float totalPorCobrar = Float.parseFloat(this.tfTotalPorCobrar.getText());
        try {
            PedidoData pedidosData = new PedidoData(this.rutaArchivoTienda);
            Pedido pedido = new Pedido(idPedido, idCliente, fecha,null,totalPorCobrar);
            pedidosData.insertar(pedido);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.layout.getScene().getWindow());
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("La información del pedido fue insertada correctamente");
            alert.showAndWait();

        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.layout.getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrio un error al abrir el archivo de datos");
            alert.showAndWait();
        }
    }
    public void handleBtnCancelar (){//TODO: VALIDACIÓN SI ESTÁ VACIA

        String idCliente = tfnumCliente.getText().trim();

        if(idCliente.isEmpty()){
            showError("El campo de Id de Cliente está vacío");
            return;
        }

        try{
            ClienteData clienteData= new ClienteData(rutaArchivoTienda);
            List<Cliente> clientes = clienteData.cargarClientes();

            Iterator<Cliente> iterator = clientes.iterator();
            boolean found = false;

            while (iterator.hasNext()){
                Cliente cliente = iterator.next();

                if(cliente.getIdCliente().trim().equals(idCliente)){
                    iterator.remove();
                    found = true;
                    break;
                }
            }

            if(found){
                clienteData.guardarLista(clientes);
                showInfo("Cliente eliminado correctamente");
            }
            else{
                showError("Cliente no encontrado");
            }
            tfnumCliente.clear();
        }
        catch (IOException e){
            showError("Ocurrió un error al intentar eliminar el cliente");
            e.printStackTrace();
        }
    }


    private void showInfo(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getLayout() {
        return layout;
    }
}
