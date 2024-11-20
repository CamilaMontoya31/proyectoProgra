package controller;

import data.ClienteData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cliente;

import java.io.IOException;
import java.util.List;

public class MostrarClientes {
    private TableView<Cliente> tablaClientes;
    private ObservableList<Cliente> listaClientes;
    private String rutaArchivo;

    public MostrarClientes(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        inicializarTabla();
        cargarDatos();
    }

    private void inicializarTabla() {
        tablaClientes = new TableView<>();
        listaClientes = FXCollections.observableArrayList();

        // Configuración de columnas
        TableColumn<Cliente, String> colId = new TableColumn<>("ID Cliente");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Agregar columnas a la tabla
        tablaClientes.getColumns().addAll(colId, colNombre, colTelefono, colDireccion);
        tablaClientes.setItems(listaClientes);
    }

    private void cargarDatos() {
        try {
            ClienteData clienteData = new ClienteData(rutaArchivo);
            List<Cliente> clientes = clienteData.cargarClientes();
            listaClientes.setAll(clientes); // Cargar los datos en la lista observable
        } catch (IOException e) {
            mostrarError("Error al cargar los clientes", e.getMessage());
        }
    }

    public void mostrarVentana() {
        Stage ventana = new Stage();
        ventana.setTitle("Lista de Clientes");

        VBox layout = new VBox(10, tablaClientes);
        layout.setPadding(new Insets(15));

        Scene escena = new Scene(layout, 600, 400);
        ventana.setScene(escena);
        ventana.show();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

