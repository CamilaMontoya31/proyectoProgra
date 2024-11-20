package controller;

import data.ClienteData;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Cliente;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class EliminarCliente {
    private VBox layout;
    private TextField tfidCliente;
    private String rutaArchivo;

    public EliminarCliente(String rutaArchivo){
        this.rutaArchivo= rutaArchivo;
        createUi();
    }

    public void createUi(){
        layout = new VBox(10);

        Label lbidCliente = new Label("ID del cliente");
        tfidCliente= new TextField();

        Button btnEliminar = new Button("Eliminar Cliente");
        btnEliminar.setOnAction(e -> handleBtnEliminar());

        layout.getChildren().addAll(lbidCliente,tfidCliente,btnEliminar);
    }

    public void handleBtnEliminar(){
        String idCliente = tfidCliente.getText().trim();

        if(idCliente.isEmpty()){
            showError("El campo de Id de Cliente está vacío");
            return;
        }

        try{
            ClienteData clienteData= new ClienteData(rutaArchivo);
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
            tfidCliente.clear();
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
