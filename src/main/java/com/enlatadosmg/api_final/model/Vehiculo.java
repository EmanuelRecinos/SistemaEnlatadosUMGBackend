package com.enlatadosmg.api_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehiculo {
    private String placa;   // clave
    private String marca;
    private String modelo;
    private String color;
    private int anio;
}

