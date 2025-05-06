package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.exception.ExcepcionRepartidorNoEncontrado;
import com.enlatadosmg.api_final.model.Repartidor;
import com.enlatadosmg.api_final.service.eLineales.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    // Crear repartidor
    @PostMapping
    public ResponseEntity<String> crearRepartidor(@RequestBody Repartidor repartidor) {
        try {
            repartidorService.insertarRepartidor(repartidor);
            return new ResponseEntity<>("Repartidor creado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el repartidor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Modificar repartidor
    @PutMapping("/{cui}")
    public ResponseEntity<String> modificarRepartidor(@PathVariable long cui, @RequestBody Repartidor repartidor) {
        try {
            repartidor.setCui(cui);
            repartidorService.modificarRepartidor(repartidor);
            return new ResponseEntity<>("Repartidor modificado exitosamente", HttpStatus.OK);
        } catch (ExcepcionRepartidorNoEncontrado e) {
            return new ResponseEntity<>("Repartidor no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al modificar el repartidor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar repartidor
    @DeleteMapping("/{cui}")
    public ResponseEntity<String> eliminarRepartidor(@PathVariable long cui) {
        try {
            repartidorService.eliminarRepartidor(cui);
            return new ResponseEntity<>("Repartidor eliminado exitosamente", HttpStatus.OK);
        } catch (ExcepcionRepartidorNoEncontrado e) {
            return new ResponseEntity<>("Repartidor no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el repartidor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Asignar repartidor a un pedido
    @PostMapping("/asignar")
    public ResponseEntity<Repartidor> asignarRepartidor() {
        try {
            Repartidor repartidor = repartidorService.asignarRepartidor();
            return new ResponseEntity<>(repartidor, HttpStatus.OK);
        } catch (ExcepcionListaVacia e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Regresar un repartidor a la cola
    @PostMapping("/regresar")
    public ResponseEntity<String> regresarRepartidor(@RequestBody Repartidor repartidor) {
        try {
            repartidorService.regresarRepartidor(repartidor);
            return new ResponseEntity<>("Repartidor regresado a la cola", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al regresar el repartidor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Mostrar Reapartidores
    @GetMapping
    public ResponseEntity<List<Repartidor>> mostrarRepartidores() {
        try {
            List<Repartidor> repartidores = repartidorService.mostrarRepartidores();
            return new ResponseEntity<>(repartidores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    // Cargar repartidores desde archivo CSV
        @PostMapping("/cargar")
    public ResponseEntity<String> cargarRepartidoresDesdeCSV(@RequestParam("archivo") MultipartFile archivoCsv) {
        try {
            repartidorService.cargarRepartidoresDesdeCSV(archivoCsv);
            return new ResponseEntity<>("Repartidores cargados correctamente desde el archivo CSV", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar los repartidores desde el archivo CSV: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

