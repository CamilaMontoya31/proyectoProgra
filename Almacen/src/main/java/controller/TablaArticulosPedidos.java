package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.DetallePedido;

import java.awt.*;

public class TablaArticulosPedidos {

    private VBox layout; //permite agregar de arriba a abajo

    public TablaArticulosPedidos() {
        createUI();
    }

    public void createUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(15));
        // Crear TableView
        TableView<ObservableList<String>> table = new TableView<>();
        table.setEditable(true);

        // Crear columnas
        TableColumn<ObservableList<String>, String> column1 = new TableColumn<>("ID Producto");
        column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        column1.setCellFactory(TextFieldTableCell.forTableColumn());

        // Crear columnas de meses y la columna "Total"
        TableColumn<ObservableList<String>, String> column2 = createMonthColumn("Producto", 1, table,true);
        TableColumn<ObservableList<String>, String> column3 = createMonthColumn("Cantidad", 2, table,true);
        TableColumn<ObservableList<String>, String> column4 = createMonthColumn("Precio", 3, table,true);


        // Columna para mostrar el total de cada fila
        TableColumn<ObservableList<String>, String> subtotal = new TableColumn<>("Subtotal");
        subtotal.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));

        // Añadir columnas a la tabla
        table.getColumns().addAll(column1, column2, column3, subtotal);

        // Añadir datos de ejemplo
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList(
                FXCollections.observableArrayList("Accesorios", "1", "70.0", "0"),
                FXCollections.observableArrayList("Bolsos", "2", "60.0", "0", "0"),
                FXCollections.observableArrayList("Blusas", "1", "45.0", "0", "0")
        );

        table.setItems(data);

        //crear Botones
        Button btnAddProducto = new Button("Agregar Producto");
      //  btnAddProducto.setOnAction(e -> handleBtnInsertarProducto);
        Button btnEliminarProducto = new Button("Eliminar Producto");
       // btnEliminarProducto.setOnAction();
        //crear grid para los botones
        GridPane gridPane = new GridPane();

        // Configurar layout
        VBox root = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(table, gridPane);

    }

    // Método para crear una columna para un mes
    private TableColumn<ObservableList<String>, String> createMonthColumn(String columnName, int columnIndex, TableView<ObservableList<String>> table, boolean isQuantity) {
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex)));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            ObservableList<String> rowData = event.getTableView().getItems().get(row);
            rowData.set(columnIndex, event.getNewValue());
            System.out.println("Nuevo valor en fila " + (row + 1) + ", columna " + columnName + ": " + event.getNewValue());

            // Solo actualizar el subtotal si las columnas de cantidad y precio están actualizadas
            if (isQuantity || !isQuantity && columnIndex == 2) {
                updateSubtotal(row, table);
            }
        });
        return column;
    }

    // Método para actualizar la columna "Total"
    private void updateSubtotal(int row, TableView<ObservableList<String>> table) {
        ObservableList<String> rowData = table.getItems().get(row);

        try {
            // Convertir la cantidad y el precio a valores numéricos
            int cantidad = Integer.parseInt(rowData.get(1));
            float precio = Float.parseFloat(rowData.get(2));

            // Calcular el subtotal
            float subtotalValue = cantidad * precio;

            // Actualizar la columna subtotal en la fila correspondiente
            rowData.set(3, String.valueOf(subtotalValue));
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir los valores de cantidad o precio a números.");
        }
    }
/*
//Metodo para insertar productos
    public void handleBtnInsertarProducto{
        // Create a new modal window(Dialog)
        Dialog<DetallePedido> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Add a new product to the order");
// Set the button
typesButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
// Search fieldTextField
searchField = new TextField();searchField.setPromptText("Enter product name");
// List view to display search
resultsListView<String> productListView = new ListView<>();
        ObservableList<String> productNames = FXCollections.observableArrayList(productPrices.keySet());
        productListView.setItems(productNames);
// Filter the list as the user typessearch
        Field.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredList = FXCollections.observableArrayList();    for (String name : productNames) {
                if (name.toLowerCase().contains(newValue.toLowerCase())) {            filteredList.add(name);
                }    }    productListView.setItems(filteredList);});

    }*/
    public VBox getLayout() {
        return layout;
    }
}
