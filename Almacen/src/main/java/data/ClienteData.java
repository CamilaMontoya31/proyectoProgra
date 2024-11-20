package data;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteData {

    private PrintWriter printWriter;
    private String rutaArchivo;
    private Scanner scanner;

    public ClienteData(String rutaArchivo) throws IOException{
        this.rutaArchivo = rutaArchivo;

        //Se asegura que el archivo existe y crea un PrintWriter
        File archivo = new File(rutaArchivo);
        if(!archivo.exists()){
            archivo.createNewFile(); //Crea el archivo si no existe
        }

        printWriter = new PrintWriter(new FileWriter(rutaArchivo,true));
        scanner = new Scanner(new FileReader(rutaArchivo));
    }

    public void guardar(Cliente cliente){
        printWriter.print(cliente.getIdCliente()+ ";");
        printWriter.print(cliente.getNombre()+";");
        printWriter.print(cliente.getTelefono()+";");
        printWriter.println(cliente.getDireccion().trim());//Crea un salto de línea
        printWriter.flush(); //Se asegura que los datos se escriban inmediatamente
    }

    // Cargar todos los clientes del archivo
    public List<Cliente> cargarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        scanner = new Scanner(new FileReader(rutaArchivo));
        scanner.useDelimiter("\n"); // Procesar línea completa por salto de línea

        while (scanner.hasNext()) {
            String linea = scanner.nextLine().trim();
            String[] partes = linea.split(";");

            // Validar si la línea tiene exactamente 4 partes
            if (partes.length == 4) {
                String idCliente = partes[0].trim();
                String nombre = partes[1].trim();
                String telefono = partes[2].trim();
                String direccion = partes[3].trim();

                clientes.add(new Cliente(idCliente, nombre, telefono, direccion));
            } else {
                System.out.println("Línea inválida encontrada y omitida: " + linea);
            }
        }
        return clientes;
    }


    // Guardar la lista de clientes actualizada en el archivo
    public void guardarLista(List<Cliente> clientes) throws IOException {
        try(PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))){
            for (Cliente cliente : clientes) {
                writer.print(cliente.getIdCliente() + ";");
                writer.print(cliente.getNombre() + ";");
                writer.print(cliente.getTelefono() + ";");
                writer.println(cliente.getDireccion().trim());
            }
        }
    }

    public void closePrinterWriter(){
        printWriter.close();
    }

    public void closeScanner(){
        scanner.close();
    }

    //TODO investigar para que se requiere el contador

    public int size(){
        int contador = 0;

        try(Scanner scanner = new Scanner(new FileReader(rutaArchivo))){
            scanner.useDelimiter(";");

            while(scanner.hasNext()){
                scanner.next();
                contador++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contador/4; //Divide entre 4 porque cada cliente tiene 4 campos de información
    }

    public Cliente[] findAll() throws IOException{
        Cliente[] cliente = new Cliente[size()];
        scanner= new Scanner(new FileReader(rutaArchivo));
        scanner.useDelimiter(";|\n"); //Delimitador de registros

        int count = 0;

        while (scanner.hasNext()){
            String idCliente = scanner.next();
            String nombre = scanner.next();
            String telefono = scanner.next();
            String direccion = scanner.next();

            cliente[count] = new Cliente(idCliente,nombre,telefono,direccion);
            count++;

            if(scanner.hasNext()){
                scanner.nextLine();
            }
        }
        return cliente;
    }

    public void eliminarClientes(String idClienteEliminar) throws IOException {
        // Lista para almacenar los clientes restantes
        List<Cliente> clientes = new ArrayList<>();

        // Abrir el archivo para leer
        try (Scanner archivoScanner = new Scanner(new File(rutaArchivo))) {
            // Leer todo el archivo
            while (archivoScanner.hasNextLine()) {
                // Leer una línea completa
                String linea = archivoScanner.nextLine();
                // Dividir la línea en sus partes usando el delimitador ";"
                String[] partes = linea.split(";");

                // Asegurarse de que la línea tiene los suficientes datos
                if (partes.length == 4) {
                    String idCliente = partes[0].trim();
                    String nombre = partes[1].trim();
                    String telefono = partes[2].trim();
                    String direccion = partes[3].trim();

                    // Si el ID del cliente no coincide con el ID a eliminar, lo agregamos a la lista
                    if (!idCliente.equals(idClienteEliminar)) {
                        clientes.add(new Cliente(idCliente, nombre, telefono, direccion));
                    }
                }
            }
        }

        // Reescribir el archivo con los clientes restantes (sin el eliminado)
        try (PrintWriter archivoActualizado = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Cliente cliente : clientes) {
                archivoActualizado.print(cliente.getIdCliente() + ";");
                archivoActualizado.print(cliente.getNombre() + ";");
                archivoActualizado.print(cliente.getTelefono() + ";");
                archivoActualizado.println(cliente.getDireccion());
            }
        }
    }












    /*private static final String FILE_NAME = "CLIENTES.DAT";

    public static List<Cliente> cargarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(", ");
                clientes.add(new Cliente(partes[0], partes[1], partes[2], partes[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /*public static void agregarCliente(String nombre, String telefono, String direccion) {
        String idCliente = Consecutivo.obtenerConsecutivoCliente();  // Genera el ID único para el cliente
        Cliente cliente = new Cliente(idCliente, nombre, telefono, direccion);  // Crea un nuevo cliente
        guardarCliente(cliente);  // Guarda el cliente en el archivo
    }*/


    /*public static void borrarCliente(String idCliente) {
        List<Cliente> clientes = cargarClientes();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Cliente cliente : clientes) {
                if (!cliente.getIdCliente().equals(idCliente)) {
                    writer.write(cliente.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void agregarCliente(String nombre, String telefono, String direccion) {
        String idCliente = Consecutivo.obtenerConsecutivoCliente();
        Cliente cliente = new Cliente(idCliente, nombre, telefono, direccion);
        guardarCliente(cliente);
    }*/
}
