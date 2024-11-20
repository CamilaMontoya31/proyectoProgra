package controller;


import data.PedidoData;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Pedido;

import java.text.SimpleDateFormat;
import java.util.*;

public class GraficoVentas {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public LineChart<String, Number> generarGraficoVentas() {
        // Configuración de los ejes
        CategoryAxis xAxis = new CategoryAxis(); // Eje X para las fechas
        NumberAxis yAxis = new NumberAxis();     // Eje Y para los totales de ventas
        xAxis.setLabel("Días de la Semana");
        yAxis.setLabel("Total de Ventas");

        // Crear el gráfico de líneas
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Ventas Semanales");

        // Crear la serie de datos para el gráfico
        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ventas");

        // Obtener los datos de los pedidos y calcular ventas diarias
        Map<String, Double> ventasDiarias = obtenerVentasDiarias();

        // Agregar los datos a la serie del gráfico
        for (Map.Entry<String, Double> entrada : ventasDiarias.entrySet()) {
            serieVentas.getData().add(new XYChart.Data<>(entrada.getKey(), entrada.getValue()));
        }

        // Agregar la serie al gráfico
        lineChart.getData().add(serieVentas);

        return lineChart;
    }

    private Map<String, Double> obtenerVentasDiarias() {
        List<Pedido> pedidos = PedidoData.cargarPedidos();
        Map<String, Double> ventasDiarias = new TreeMap<>();

        for (Pedido pedido : pedidos) {
            String fecha = DATE_FORMAT.format(pedido.getFecha());
            ventasDiarias.put(fecha, ventasDiarias.getOrDefault(fecha, 0.0) + pedido.getTotal());
        }

        return ventasDiarias;
    }
}

