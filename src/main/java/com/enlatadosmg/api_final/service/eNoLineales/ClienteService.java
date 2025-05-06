package com.enlatadosmg.api_final.service.eNoLineales;

import com.enlatadosmg.api_final.exception.ExcepcionClienteNoEncontrado;
import com.enlatadosmg.api_final.model.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    private ArbolAVL arbolClientes;


    public ClienteService() {
        arbolClientes = new ArbolAVL();
    }

    // Crear cliente
    public void insertarCliente(Cliente cliente) {
        logger.info("Intentando insertar cliente con CUI: {}", cliente.getCui());
        arbolClientes.insertar(cliente);
        logger.info("Cliente con CUI {} insertado correctamente.", cliente.getCui());
    }

    // Buscar cliente
    public Cliente buscarCliente(long cui) {
        logger.info("Buscando cliente con CUI: {}", cui);
        Cliente cliente = arbolClientes.buscar(cui);
        if (cliente == null) {
            logger.warn("Cliente con CUI {} no encontrado.", cui);
            throw new ExcepcionClienteNoEncontrado("Cliente con CUI " + cui + " no encontrado.");
        }
        logger.info("Cliente con CUI {} encontrado.", cui);
        return cliente;
    }

    // Modificar cliente
    public void modificarCliente(Cliente cliente) {
        logger.info("Modificando cliente con CUI: {}", cliente.getCui());
        Cliente existente = arbolClientes.buscar(cliente.getCui());
        if (existente == null) {
            logger.warn("No se puede modificar. Cliente con CUI {} no encontrado.", cliente.getCui());
            throw new ExcepcionClienteNoEncontrado("No se puede modificar. Cliente con CUI " + cliente.getCui() + " no encontrado.");
        }
        arbolClientes.modificar(cliente);
        logger.info("Cliente con CUI {} modificado con éxito.", cliente.getCui());
    }

    // Eliminar cliente
    public void eliminarCliente(long cui) {
        logger.info("Eliminando cliente con CUI: {}", cui);
        Cliente existente = arbolClientes.buscar(cui);
        if (existente == null) {
            logger.warn("No se puede eliminar. Cliente con CUI {} no encontrado.", cui);
            throw new ExcepcionClienteNoEncontrado("No se puede eliminar. Cliente con CUI " + cui + " no encontrado.");
        }
        arbolClientes.eliminar(cui);
        logger.info("Cliente con CUI {} eliminado con éxito.", cui);
    }

    // Cargar clientes desde un archivo CSV
    public void cargarClientesDesdeCSV(MultipartFile archivoCsv) throws IOException {
        logger.info("Iniciando carga de clientes desde archivo CSV.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivoCsv.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    long cui = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    String apellido = datos[2].trim();
                    String telefono = datos[3].trim();
                    String direccion = datos[4].trim();
                    Cliente cliente = new Cliente(cui, nombre, apellido, telefono, direccion);
                    insertarCliente(cliente);
                    logger.info("Cliente con CUI {} cargado correctamente desde CSV.", cui);
                }
            }
        } catch (IOException e) {
            logger.error("Error al procesar el archivo CSV: {}", e.getMessage());
            throw new IOException("Error al procesar el archivo CSV: " + e.getMessage());
        }
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        logger.info("Obteniendo todos los clientes.");
        List<Cliente> clientes = new ArrayList<>();
        arbolClientes.recorridoInorden(clientes);
        logger.info("Total de clientes obtenidos: {}", clientes.size());
        return clientes;
    }

    // Mostrar todos los clientes (inorden)
    public void mostrarClientes() {
        logger.info("Mostrando todos los clientes en orden.");
        arbolClientes.recorridoInorden();
    }
}

