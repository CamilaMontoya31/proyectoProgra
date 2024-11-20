package vista;

import controller.*;
import data.ArticulosData;
import data.GenerarId;
import data.PedidoData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Articulo;

import java.io.IOException;
import java.security.cert.PolicyNode;
import java.util.List;

public class MainApp extends Application {

    private BorderPane rootLayout;
    private final String rutaArchivo = "D:\\Clientes\\clientesDef";
    private VBox vBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Pedidos");

        // Configurar el BorderPane principal
        rootLayout = new BorderPane();
        rootLayout.setTop(crearMenuBar());

        // Crear la escena principal
        Scene scene = new Scene(rootLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public MenuBar crearMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menú Sistema
        Menu menuSistema = new Menu("Sistema");
        MenuItem menuItemAcercaDe = new MenuItem("Acerca de");
        menuItemAcercaDe.setOnAction(e -> mostrarAcercaDe());
        MenuItem menuItemSalir = new MenuItem("Salir");
        menuItemSalir.setOnAction(e -> Platform.exit());
        menuSistema.getItems().addAll(menuItemAcercaDe, menuItemSalir);

        // Menú Mantenimiento
        Menu menuMantenimiento = new Menu("Mantenimiento");

        // Submenú Insertar
        Menu subMenuInsertar = new Menu("Insertar");
        MenuItem insertarCliente = new MenuItem("Cliente");
        insertarCliente.setOnAction(e -> insertarCliente());
        MenuItem insertarArticulo = new MenuItem("Artículo");
        insertarArticulo.setOnAction(e -> abrirVentanaInsertarArticulo());
        subMenuInsertar.getItems().addAll(insertarCliente, insertarArticulo);

        // Submenú Borrar
        Menu subMenuBorrar = new Menu("Borrar");
        MenuItem borrarCliente = new MenuItem("Cliente");
        borrarCliente.setOnAction(e -> eliminarCliente());
        MenuItem borrarArticulo = new MenuItem("Artículo");
        borrarArticulo.setOnAction(e -> abrirVentanaEliminarArticulo());
        subMenuBorrar.getItems().addAll(borrarCliente, borrarArticulo);

        menuMantenimiento.getItems().addAll(subMenuInsertar, subMenuBorrar);
        //Menú Transsaciones
        Menu menuTransaciones = new Menu("Transacciones");
        MenuItem menuItemInsertar = new MenuItem("Nuevo pedido");
        menuItemInsertar.setOnAction(e -> showInsertarPedidosController());
        MenuItem menuItemListar = new MenuItem("Cierre del día");
        menuTransaciones.getItems().add(menuItemInsertar);
        menuTransaciones.getItems().add(menuItemListar);
      //  menuBar.getMenus().add(menuTransaciones);


        // Menú Reportes
        Menu menuReportes = new Menu("Reportes");
        MenuItem menuItemListadoClientes = new MenuItem("Listado de Clientes");
        menuItemListadoClientes.setOnAction(e -> {
            MostrarClientes mostrarClientes = new MostrarClientes(rutaArchivo);
            mostrarClientes.mostrarVentana();
        });
        menuReportes.getItems().add(menuItemListadoClientes);

        menuBar.getMenus().addAll(menuSistema, menuTransaciones, menuMantenimiento, menuReportes);
        return menuBar;
    }

    public void insertarCliente() {
        try {
            GenerarId generarId = new GenerarId(rutaArchivo + "idClientes.txt");
            GuardarCliente guardarCliente = new GuardarCliente(rutaArchivo, generarId);
            VBox layout = guardarCliente.getLayout();

            if (layout != null) {
                Stage ventanaInsertarCliente = new Stage();
                ventanaInsertarCliente.setTitle("Insertar Cliente");
                Scene scene = new Scene(layout, 400, 300);
                ventanaInsertarCliente.setScene(scene);
                ventanaInsertarCliente.show();
            } else {
                mostrarMensajeError("Error", "No se pudo cargar el formulario de cliente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error", "Ocurrió un error al intentar insertar un cliente.");
        }
    }

    public void eliminarCliente() {
        try {
            EliminarCliente eliminarCliente = new EliminarCliente(rutaArchivo);
            VBox layout = eliminarCliente.getLayout();

            if (layout != null) {
                Stage ventanaEliminarCliente = new Stage();
                ventanaEliminarCliente.setTitle("Eliminar Cliente");
                Scene scene = new Scene(layout, 400, 300);
                ventanaEliminarCliente.setScene(scene);
                ventanaEliminarCliente.show();
            } else {
                mostrarMensajeError("Error", "No se pudo cargar el formulario de eliminación de cliente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error", "Ocurrió un error al intentar eliminar un cliente.");
        }
    }

    private void abrirVentanaEliminarArticulo() {
        Stage ventanaEliminarArticulo = new Stage();
        ventanaEliminarArticulo.initModality(Modality.APPLICATION_MODAL);
        ventanaEliminarArticulo.setTitle("Eliminar Artículo");

        // Campo de texto para búsqueda
        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Ingrese término de búsqueda...");

        // Lista para mostrar todos los artículos o los resultados de búsqueda
        ListView<Articulo> listaArticulos = new ListView<>();
        listaArticulos.getItems().setAll(ArticulosData.cargarArticulos());

        // Botón para buscar artículos
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            String termino = campoBusqueda.getText();
            if (termino.isEmpty()) {
                listaArticulos.getItems().setAll(ArticulosData.cargarArticulos());
            } else {
                List<Articulo> resultados = ArticulosData.buscarProducto(termino);
                listaArticulos.getItems().setAll(resultados);

                if (resultados.isEmpty()) {
                    mostrarMensajeError("Sin resultados", "No se encontraron artículos que coincidan con el término.");
                }
            }
        });

        // Botón para eliminar el artículo seleccionado
        Button btnEliminar = new Button("Eliminar Artículo");
        btnEliminar.setOnAction(e -> {
            Articulo articuloSeleccionado = listaArticulos.getSelectionModel().getSelectedItem();
            if (articuloSeleccionado == null) {
                mostrarMensajeError("Error", "Debe seleccionar un artículo para eliminar.");
                return;
            }

            // Verificar si el artículo está asociado a una venta
            if (PedidoData.estaAsociadoAVenta(articuloSeleccionado.getIdArticulo())) {
                mostrarMensajeError("No permitido", "El artículo está asociado a una venta y no se puede eliminar.");
                return;
            }

            // Eliminar el artículo
            boolean eliminado = ArticulosData.eliminarArticulo(articuloSeleccionado.getIdArticulo());
            if (eliminado) {
                mostrarMensajeExito("Éxito", "El artículo fue eliminado correctamente.");
                listaArticulos.getItems().setAll(ArticulosData.cargarArticulos());
            } else {
                mostrarMensajeError("Error", "No se pudo eliminar el artículo seleccionado.");
            }
        });

        // Diseño de la ventana
        VBox layout = new VBox(
                new Label("Eliminar Artículo"),
                campoBusqueda,
                btnBuscar,
                new Label("Lista de Artículos:"),
                listaArticulos,
                btnEliminar
        );
        layout.setSpacing(10);

        Scene scene = new Scene(layout, 400, 400);
        ventanaEliminarArticulo.setScene(scene);
        ventanaEliminarArticulo.show();
    }

    private void abrirVentanaInsertarArticulo() {
        Stage ventanaInsertarArticulo = new Stage();
        ventanaInsertarArticulo.initModality(Modality.APPLICATION_MODAL);
        ventanaInsertarArticulo.setTitle("Insertar Artículo");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del Artículo");

        TextField descripcionField = new TextField();
        descripcionField.setPromptText("Descripción del Artículo");

        TextField precioField = new TextField();
        precioField.setPromptText("Precio del Artículo");

        TextField cantidadField = new TextField();
        cantidadField.setPromptText("Cantidad en Inventario");

        Button btnAgregar = new Button("Guardar producto");
        btnAgregar.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                String descripcion = descripcionField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int cantidad = Integer.parseInt(cantidadField.getText());

                if (nombre.isEmpty() || descripcion.isEmpty() || precio <= 0 || cantidad < 0) {
                    mostrarMensajeError("Error", "Todos los campos deben estar llenos, el precio mayor a 0 y la cantidad no puede ser negativa.");
                    return;
                }

                Articulo articulo = new Articulo(null, nombre + " - " + descripcion, precio, cantidad);
                ArticulosData.guardarArticulo(articulo);
                mostrarMensajeExito("Éxito", "Artículo agregado correctamente.");
                ventanaInsertarArticulo.close();
            } catch (NumberFormatException ex) {
                mostrarMensajeError("Error", "El precio y la cantidad deben ser valores válidos.");
            }
        });

        VBox vbox = new VBox(
                new Label("Nombre:"), nombreField,
                new Label("Descripción:"), descripcionField,
                new Label("Precio:"), precioField,
                new Label("Cantidad:"), cantidadField,
                btnAgregar
        );
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 300, 350);
        ventanaInsertarArticulo.setScene(scene);
        ventanaInsertarArticulo.show();
    }

    //Acción insertar pedidos
    public void showInsertarPedidosController(){
        InsertarPedidosController insertarController = new InsertarPedidosController(rutaArchivo);
        this.vBox.getChildren().add(insertarController.getLayout());
        TablaArticulosPedidos mostrarTabla = new TablaArticulosPedidos();
        this.vBox.getChildren().add(mostrarTabla.getLayout());

    }
    private void mostrarMensajeError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensajeExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAcercaDe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Sistema de Pedidos");
        alert.setContentText("Versión 1.0\nDesarrollado para la gestión de clientes y artículos.");
        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

