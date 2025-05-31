package com.enlatadosmg.api_final.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LimpiadorDeReportes {

    @PostConstruct
    public void limpiarArchivos() {
        String[] archivos = {
                "archivos/reporte_almacen.png",
                "archivos/almacen.dot",
                "archivos/reporte_usuarios.png",
                "archivos/reporte_usuarios.dot",

        };

        for (String path : archivos) {
            File archivo = new File(path);
            if (archivo.exists()) {
                archivo.delete();
            }
        }
    }
}
