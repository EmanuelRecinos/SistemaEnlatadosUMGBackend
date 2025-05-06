package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Cliente;
import com.enlatadosmg.api_final.service.eNoLineales.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Crear cliente
    @PostMapping("/crear")
    public String crearCliente(@RequestBody Cliente cliente) {
        clienteService.insertarCliente(cliente);
        return "Cliente creado con éxito.";
    }

    // Buscar cliente
    @GetMapping("/{cui}")
    public Cliente buscarCliente(@PathVariable long cui) {
        return clienteService.buscarCliente(cui);
    }

    // Modificar cliente
    @PutMapping("/modificar")
    public String modificarCliente(@RequestBody Cliente cliente) {
        clienteService.modificarCliente(cliente);
        return "Cliente modificado con éxito.";
    }

    // Eliminar cliente
    @DeleteMapping("/eliminar/{cui}")
    public String eliminarCliente(@PathVariable long cui) {
        clienteService.eliminarCliente(cui);
        return "Cliente eliminado con éxito.";
    }

    // Cargar clientes desde un archivo CSV
    @PostMapping("/cargar")
    public String cargarClientesDesdeCSV(@RequestParam("archivoCsv") MultipartFile archivoCsv) {
        try {
            clienteService.cargarClientesDesdeCSV(archivoCsv);
            return "Clientes cargados con éxito desde el archivo.";
        } catch (IOException e) {
            return "Error al cargar el archivo: " + e.getMessage();
        }
    }

    // Mostrar todos los clientes (en formato JSON)
    @GetMapping("/todos")
    public List<Cliente> mostrarClientes() {
        return clienteService.obtenerTodosLosClientes();
    }
}

