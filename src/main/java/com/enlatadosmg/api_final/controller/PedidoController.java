package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.dto.CrearPedidoDTO;
import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.Pedido;
import com.enlatadosmg.api_final.service.eLineales.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoDTO dto) {
        try {
            Pedido pedido = pedidoService.crearPedido(
                    dto.getDepartamentoOrigen(),
                    dto.getDepartamentoDestino(),
                    dto.getCuiCliente(),
                    dto.getCantidadCajas()
            );
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Si no hay cajas suficientes para el pedido
        } catch (ExcepcionListaVacia | IOException e) {
            return ResponseEntity.status(500).body("Error del servidor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        Pedido pedido = pedidoService.buscarPorNumero(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> completarPedido(@PathVariable int id) {
        try {
            boolean actualizado = pedidoService.completarPedido(id);
            if (actualizado) {
                return ResponseEntity.ok("Pedido completado exitosamente");
            }
            return ResponseEntity.badRequest().body("No se encontró el pedido o ya estaba completado");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al devolver recursos: " + e.getMessage());
        }
    }
}
