package controller;

import data.ClienteData;
import data.GenerarId;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Cliente;

import java.io.File;
import java.io.IOException;

public class GuardarCliente {
    private VBox layout;
    private String rutaArchivo;
    private TextField tfIdCliente, tfNombre, tfTelefono, tfDieccion;
    private GenerarId generarId;

    public GuardarCliente(String rutaArchivo,GenerarId generarId) throws IOException {
        this.rutaArchivo= rutaArchivo;
        this.generarId = generarId;
        creatIu();

    }

    public void creatIu(){
        layout = new VBox(10);
        layout.setPadding(new Insets(15));

        String idGenerado = generarId.generarNuevoId();
        //System.out.println("Nuevo Id generado" + idGenerado); //Prueba temporal

        //Label del título
        Label lbTitulo = new Label("Insertar un nuevo cliente");
        layout.getChildren().add(lbTitulo);

        Label lbIdCliente = new Label("Id Cliente");
        tfIdCliente = new TextField(idGenerado);
        tfIdCliente.setEditable(false);

        Label lbNombre = new Label("Nombre");
        tfNombre = new TextField();

        Label lbTelefono = new Label("Teléfono");
        tfTelefono = new TextField();

        Label lbDireccion = new Label("Dirección");
        tfDieccion = new TextField();

        GridPane pane = new GridPane();
        pane.add(lbIdCliente, 0, 0);
        pane.add(tfIdCliente, 1, 0);

        pane.add(lbNombre, 0, 1);
        pane.add(tfNombre, 1, 1);

        pane.add(lbTelefono, 0, 2);
        pane.add(tfTelefono, 1, 2);

        pane.add(lbDireccion, 0, 3);
        pane.add(tfDieccion, 1, 3);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e ->handleBtnGuardar());

        Button btnBorrar = new Button("Cancelar");

        pane.add(btnGuardar, 0, 4);
        pane.add(btnBorrar, 1, 4);
        this.layout.getChildren().add(pane);
    }

    public void handleBtnGuardar(){
        String idCliente = this.tfIdCliente.getText();
        String nombre = this.tfNombre.getText();
        String telefono= this.tfTelefono.getText();
        String direccion= this.tfDieccion.getText();

        if (!telefono.matches("[2-8]{1}[0-9]{7}")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.getLayout().getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("El formato de teléfono ingresado no es válido. Debe ingresar un número de 8 dígitos que comience con un número de 2-8 sin lineas ni espacios ");
            alert.showAndWait();
            return;
        }

        if(nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.getLayout().getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Todos los espacios deben ser completados");
            alert.showAndWait();
            return;
        }

        try {

            //Verificar si el archivo existe, si no, crear uno
            File archivo= new File(rutaArchivo);

            if (!archivo.exists()) {
                boolean creado = archivo.createNewFile();
                if (creado) {
                    System.out.println("Archivo creado correctamente.");
                } else {
                    System.out.println("No se pudo crear el archivo.");
                }
            }

            // Verificar si el archivo se puede leer
            if (!archivo.canRead() || !archivo.canWrite()) {
                System.out.println("El archivo no tiene permisos para leer o escribir.");
            }


            ClienteData clienteData = new ClienteData(this.rutaArchivo);
            Cliente cliente = new Cliente(idCliente,nombre,telefono,direccion);
            clienteData.guardar(cliente);

            Alert alertCorrect = new Alert(Alert.AlertType.INFORMATION);
            alertCorrect.initOwner(this.getLayout().getScene().getWindow());
            alertCorrect.setTitle("Información");
            alertCorrect.setHeaderText(null);
            alertCorrect.setContentText("La información del cliente se ha guardado de manera correcta");
            alertCorrect.showAndWait();

            //Limpiar espacios después de guardar información
            this.tfIdCliente.clear();
            this.tfNombre.clear();
            this.tfTelefono.clear();
            this.tfDieccion.clear();

            String nuevoId = generarId.generarNuevoId();
            tfIdCliente.setText(nuevoId);
            //System.out.println("Nuevo Id generado" + nuevoId); //Prueba temporal

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.getLayout().getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al querer guardar la información");
            alert.showAndWait();
            e.printStackTrace();

        }
    }

    public VBox getLayout(){
        return layout;
    }

}