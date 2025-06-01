package com.enlatadosmg.api_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private int numeroPedido;
    private String departamentoOrigen;
    private String departamentoDestino;
    private LocalDateTime fechaHoraInicio;
    private String estado;

    private Cliente cliente;
    private List<Caja> cajas;
    private Repartidor repartidor;
    private Vehiculo vehiculo;

    @Override
    public String toString() {
        return String.format(
                "Pedido #%d | %s → %s | Cliente: %s | Cajas: %d | Repartidor: %s | Vehículo: %s | Estado: %s",
                numeroPedido, departamentoOrigen, departamentoDestino,
                cliente.getNombre(), cajas.size(),
                repartidor.getNombre(), vehiculo.getPlaca(),
                estado
        );
    }
}
