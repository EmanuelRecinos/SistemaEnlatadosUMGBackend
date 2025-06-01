package com.enlatadosmg.api_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caja {

    private int correlativo; //clave
    private LocalDate fechaIngreso;
}
