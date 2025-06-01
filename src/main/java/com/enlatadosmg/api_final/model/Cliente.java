package com.enlatadosmg.api_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Long cui; //clave
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;


}
