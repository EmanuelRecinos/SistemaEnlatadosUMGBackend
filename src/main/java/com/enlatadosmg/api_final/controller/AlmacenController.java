package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Caja;
import com.enlatadosmg.api_final.service.eLineales.AlmacenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
