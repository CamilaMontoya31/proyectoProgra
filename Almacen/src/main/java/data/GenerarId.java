package data;

import java.io.*;

public class GenerarId {
    private String rutaArchivo;

    public GenerarId(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isEmpty()){
            throw new IllegalArgumentException("La ruta del archivo de Id no puede ser nula o vacía");
        }
        this.rutaArchivo = rutaArchivo;
    }

    public String generarNuevoId() {
        int ultimoId = leerUltimoId();
        int nuevoId = ultimoId + 1;
        guardarUltimoId(nuevoId);
        return String.format("C%04d", nuevoId); // Formato C0001, C0002, etc.
    }

    private int leerUltimoId() {

        File archivo = new File(rutaArchivo);
        if (!archivo.exists()){
            System.out.println("Archivo Id no encontrado.");
            return 0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine();
            return linea != null ? Integer.parseInt(linea) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0; // Si no hay archivo o está vacío, el primer ID será 1.
        }
    }

    private void guardarUltimoId(int id) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
