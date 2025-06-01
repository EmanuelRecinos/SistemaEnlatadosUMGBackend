package com.enlatadosmg.api_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repartidor {

    private long cui; // clave
    private String nombre;
    private String apellidos;
    private String licencia; // Tipo A, B, C
    private String telefono;


}

