package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Cliente;
import com.enlatadosmg.api_final.service.eNoLineales.ClienteService;
import com.enlatadosmg.api_final.util.ReporteCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService service;

        @GetMapping("/todos")
    public List<Cliente> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{cui}")
    public Cliente obtenerPorCui(@PathVariable long cui) {
        return service.obtenerPorCui(cui);
    }

    @PostMapping("/agregar")
    public void agregar(@RequestBody Cliente cliente) throws IOException {
        service.agregarCliente(cliente);
    }

    @PutMapping("/actualizar/{cui}")
    public boolean actualizar(@PathVariable long cui, @RequestBody Cliente nuevo) throws IOException {
        return service.actualizarCliente(cui, nuevo);
    }

    @DeleteMapping("/eliminar/{cui}")
    public boolean eliminar(@PathVariable long cui) throws IOException {
        return service.eliminarCliente(cui);
    }

    @PostMapping("/cargar")
    public void cargarDesdeCsv(@RequestParam(defaultValue = "csv/clientes.csv") String path) throws IOException {
        service.cargarDesdeCsv(path);
    }

    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) throws IOException {
        ReporteCliente.generarReporte(service.getArbolClientes());
        File imagen = new File("src/main/resources/static/reporte_clientes.png");

        if (imagen.exists()) {
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=reporte_clientes.png");

            // 🔥 Forzar que no se almacene en caché
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            Files.copy(imagen.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
