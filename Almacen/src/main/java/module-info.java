module com.example.almacen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.almacen to javafx.fxml;
    exports com.example.almacen;
    exports controller;
    exports data;
    exports model;
    exports vista;
}