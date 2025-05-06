package com.enlatadosmg.api_final.model;

import lombok.Data;

@Data
public class Usuario {

    private Long id;
    private String nombre;
    private String apellidos;
    private String contraseña;


    public Usuario(Long id, String nombre, String apellidos, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contraseña = contraseña;
    }

}
    