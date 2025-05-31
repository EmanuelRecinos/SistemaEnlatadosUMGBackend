package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Caja;
import com.enlatadosmg.api_final.service.eLineales.AlmacenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/almacen")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarCaja() {
        almacenService.agregarCaja();
        return ResponseEntity.ok("Caja agregada al almacén.");
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirarCaja() {
        Caja caja = almacenService.retirarCaja();
        if (caja == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay cajas en el almacén.");
        }
        return ResponseEntity.ok(caja);
    }

    @GetMapping("/listar")
    public List<Caja> listarCajas() {
        return almacenService.listarCajas();
    }

    @PostMapping("/cargar-inicial")
    public ResponseEntity<String> cargarInicial(@RequestParam int cantidad) {
        almacenService.cargarCajasIniciales(cantidad);
        return ResponseEntity.ok(cantidad + " cajas agregadas al almacén.");
    }

    @GetMapping("/reporte")
    public ResponseEntity<String> generarReporte() {
        String resultado = almacenService.generarReporte();

        // Adjunta un parámetro dinámico para forzar recarga del recurso en el frontend
        String urlConTimestamp = "/api/almacen/imagen?t=" + Instant.now().toEpochMilli();
        return ResponseEntity.ok(urlConTimestamp);
    }

    @GetMapping("/imagen")
    public ResponseEntity<InputStreamResource> obtenerImagenReporte() throws IOException {
        File archivoImagen = new File("archivos/reporte_almacen.png");

        if (!archivoImagen.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        InputStream inputStream = new FileInputStream(archivoImagen);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)  // Establecemos el tipo de contenido como PNG
                .body(resource);
    }
}
