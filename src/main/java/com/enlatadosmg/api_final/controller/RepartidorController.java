package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.Repartidor;
import com.enlatadosmg.api_final.service.eLineales.RepartidorService;
import com.enlatadosmg.api_final.util.ReporteRepartidor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/repartidores")
@CrossOrigin(origins = "*")
public class RepartidorController {

    @Autowired
    private RepartidorService service;

    @GetMapping("/todos")
    public List<Repartidor> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{cui}")
    public Repartidor obtenerPorCui(@PathVariable long cui) {
        return service.obtenerPorCui(cui);
    }

    @PostMapping("/agregar")
    public void agregar(@RequestBody Repartidor repartidor) throws IOException {
        service.agregarRepartidor(repartidor);
    }

    @PutMapping("/actualizar/{cui}")
    public boolean actualizar(@PathVariable long cui, @RequestBody Repartidor nuevo) throws IOException {
        return service.actualizarRepartidor(cui, nuevo);
    }

    @DeleteMapping("/eliminar/{cui}")
    public boolean eliminar(@PathVariable long cui) throws IOException {
        return service.eliminarRepartidor(cui);
    }

    @PostMapping("/asignar")
    public Repartidor asignar() throws ExcepcionListaVacia, IOException {
        return service.asignarRepartidor();
    }

    @PostMapping("/devolver")
    public void devolver(@RequestBody Repartidor repartidor) throws IOException {
        service.devolverRepartidor(repartidor);
    }

    @PostMapping("/cargar")
    public void cargarDesdeCsv(@RequestParam(defaultValue = "csv/repartidores.csv") String path) throws IOException {
        service.cargarDesdeCsv(path);
    }

    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) throws IOException {
        ReporteRepartidor.generarReporte(service.obtenerListaInterna());
        File imagen = new File("src/main/resources/static/reporte_repartidores.png");

        if (imagen.exists()) {
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=reporte_repartidores.png");

            // 🔥 Estas cabeceras fuerzan que nunca se guarde en caché
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
