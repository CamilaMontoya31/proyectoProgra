package data;

import model.Cliente;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDataTest {

    @Test
    void guardar() {
        //Arrange
        String ruta = "D:\\Clientes\\clientes1";

        try{
            ClienteData clienteData = new ClienteData(ruta);

            //Act
            Cliente clientePorInsertar = new Cliente("127492", "Oliver", "8794-1572", "San Pedro");
            clienteData.guardar(clientePorInsertar);
            clienteData.closePrinterWriter();
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
            throw new RuntimeException(e);
        }

    }


    @Test
    void size() {

        try{
            String ruta = "D:\\Clientes\\clientesTamaño";
            File archivo = new File(ruta);
            archivo.delete();

            ClienteData clienteData = new ClienteData(ruta);

            Cliente cLientePorInsertar = new Cliente("675483", "Oscar","7653-9284", "Cartago");
            Cliente cLientePorInsertar1 = new Cliente("85423", "Christian","7521-0233", "El Molino");
            Cliente cLientePorInsertar2 = new Cliente("624974", "Carlos","1207-8542", "Escazu");

            clienteData.guardar(cLientePorInsertar);
            clienteData.guardar(cLientePorInsertar1);
            clienteData.guardar(cLientePorInsertar2);

            clienteData.closePrinterWriter();

            //Act
            int cantidadRegistros = clienteData.size();
            clienteData.closeScanner();

            //Assert
            int cantidadRegistrosEsperados = 3;
            assertEquals(cantidadRegistrosEsperados,cantidadRegistros);
        } catch (IOException e) {
            assert (false);
        }

    }

    @Test
    void findAll() {
        //Arrange
        String ruta = "D:\\Clientes\\clientesDef";
        File archivo = new File(ruta);
        archivo.delete();

        try {
            ClienteData clienteData= new ClienteData(ruta);

            Cliente cliente = new Cliente("64382", "Elizabeth", "7462-7931","Cartago");
            Cliente cliente1 = new Cliente("993521", "Mackenzie", "7462-7931","Escazu");
            Cliente cliente2 = new Cliente("59257", "Charlotte", "8253-5385","San Pedro");

            clienteData.guardar(cliente);
            clienteData.guardar(cliente1);
            clienteData.guardar(cliente2);

            //Act
            Cliente[] clientes = clienteData.findAll();
            clienteData.closeScanner();

            int cantidadRegistrosEsperados = 3;
            assertEquals(cantidadRegistrosEsperados,clientes.length);

        } catch (IOException e) {
            assert (false);
        }
    }
}