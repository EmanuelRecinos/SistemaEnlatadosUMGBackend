package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Vehiculo;
import com.enlatadosmg.api_final.service.eLineales.VehiculoService;
import com.enlatadosmg.api_final.util.ReporteVehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoService service;

    @GetMapping("/todos")
    public List<Vehiculo> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{placa}")
    public Vehiculo obtenerPorPlaca(@PathVariable String placa) {
        return service.obtenerPorPlaca(placa);
    }

    @PostMapping("/agregar")
    public void agregar(@RequestBody Vehiculo vehiculo) throws IOException {
        service.agregarVehiculo(vehiculo);
    }

    @PutMapping("/actualizar/{placa}")
    public boolean actualizar(@PathVariable String placa, @RequestBody Vehiculo nuevo) throws IOException {
        return service.actualizarVehiculo(placa, nuevo);
    }

    @DeleteMapping("/eliminar/{placa}")
    public boolean eliminar(@PathVariable String placa) throws IOException {
        return service.eliminarVehiculo(placa);
    }

    @PostMapping("/cargar")
    public void cargarDesdeCsv(@RequestParam(defaultValue = "csv/vehiculos.csv") String path) throws IOException {
        service.cargarDesdeCsv(path);
    }

    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) throws IOException {
        ReporteVehiculo.generarReporte(service.obtenerListaInterna());
        File imagen = new File("src/main/resources/static/reporte_vehiculos.png");

        if (imagen.exists()) {
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=reporte_vehiculos.png");

            // Para que no se guarde el caché
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
