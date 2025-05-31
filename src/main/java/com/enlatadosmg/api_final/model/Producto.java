package com.enlatadosmg.api_final.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private String codigoProducto;
    private String nombre;
    private String descripcion;
    private int cantidad;  // cantidad en la caja (puede ser 1)
}
