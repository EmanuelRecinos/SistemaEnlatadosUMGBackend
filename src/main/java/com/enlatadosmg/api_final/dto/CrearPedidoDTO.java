package com.enlatadosmg.api_final.dto;

import lombok.Data;

@Data
public class CrearPedidoDTO {
    private String departamentoOrigen;
    private String departamentoDestino;
    private long cuiCliente;
    private int cantidadCajas;
}
