package data;

import model.Articulo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArticulosData {

    private static final String FILE_NAME = "H:\\UCR\\archivosTienda\\articulos.txt";
    private static int contadorId = 1;

    static {
        List<Articulo> articulos = cargarArticulos();
        if (!articulos.isEmpty()) {
            try {
                int maxId = 0;
                for (Articulo articulo : articulos) {
                    int idActual = Integer.parseInt(articulo.getIdArticulo());
                    if (idActual > maxId) {
                        maxId = idActual;
                    }
                }
                contadorId = maxId + 1;
            } catch (NumberFormatException e) {
                System.err.println("Error al inicializar el contador de IDs.");
            }
        }
    }

    private static String generarIdArticulo() {
        List<Articulo> articulos = cargarArticulos();
        List<String> idsExistentes = new ArrayList<>();
        for (Articulo articulo : articulos) {
            idsExistentes.add(articulo.getIdArticulo());
        }

        String nuevoId;
        do {
            nuevoId = String.format("%03d", contadorId++);
        } while (idsExistentes.contains(nuevoId));

        return nuevoId;
    }

    public static List<Articulo> cargarArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("El archivo no existe: " + FILE_NAME);
            return articulos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length == 4) {
                    try {
                        articulos.add(new Articulo(
                                partes[0], partes[1],
                                Double.parseDouble(partes[2]),
                                Integer.parseInt(partes[3])));
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear datos en línea: " + linea);
                    }
                } else {
                    System.err.println("Formato inválido en línea: " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(articulos, Comparator.comparing(Articulo::getDescripcion));
        return articulos;
    }

    public static void guardarArticulos(List<Articulo> articulos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Articulo articulo : articulos) {
                writer.write(articulo.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarArticulo(Articulo articulo) {
        List<Articulo> articulos = cargarArticulos();
        articulo = new Articulo(generarIdArticulo(), articulo.getDescripcion(), articulo.getPrecio(), articulo.getCantidad());
        articulos.add(articulo);
        guardarArticulos(articulos);
    }

    public static boolean eliminarArticulo(String idArticulo) {
        List<Articulo> articulos = cargarArticulos();
        boolean eliminado = false;

        for (int i = 0; i < articulos.size(); i++) {
            if (articulos.get(i).getIdArticulo().equals(idArticulo)) {
                articulos.remove(i);
                eliminado = true;
                break;
            }
        }

        if (eliminado) {
            guardarArticulos(articulos);
        }

        return eliminado;
    }

    /**
     * Busca productos en la lista que coincidan con el término dado.
     * @param termino Término de búsqueda.
     * @return Lista de productos que coincidan con el término.
     */
    public static List<Articulo> buscarProducto(String termino) {
        List<Articulo> articulos = cargarArticulos();
        List<Articulo> resultados = new ArrayList<>();

        for (Articulo articulo : articulos) {
            if (articulo.getDescripcion().toLowerCase().contains(termino.toLowerCase())) {
                resultados.add(articulo);
            }
        }

        return resultados;
    }
}
