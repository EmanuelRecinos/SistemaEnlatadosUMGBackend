package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Vehiculo;
import com.enlatadosmg.api_final.service.eLineales.Nodo;
import com.enlatadosmg.api_final.service.eLineales.Lista;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReporteVehiculo {

    public static void generarReporte(Lista lista) throws IOException {
        StringBuilder dot = new StringBuilder("digraph G {\n");
        dot.append("rankdir=LR;\n");
        dot.append("size=\"10,5!\";\n");
        dot.append("dpi=200;\n");
        dot.append("node [shape=record];\n");

        Nodo actual = lista.getIni();
        while (actual != null) {
            Vehiculo v = (Vehiculo) actual.getData();
            dot.append("V").append(v.getPlaca()).append("[label=\"{Placa: ").append(v.getPlaca())
                    .append(" | Marca: ").append(v.getMarca())
                    .append(" | Modelo: ").append(v.getModelo())
                    .append(" | Año: ").append(v.getAnio()).append("}\"];\n");

            if (actual.getSig() != null) {
                Vehiculo siguiente = (Vehiculo) actual.getSig().getData();
                dot.append("V").append(v.getPlaca()).append(" -> V").append(siguiente.getPlaca()).append(";\n");
            }
            actual = actual.getSig();
        }

        dot.append("}\n");

        // Guardar archivo .dot
        File dotFile = new File("src/main/resources/static/reporte_vehiculos.dot");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFile))) {
            writer.write(dot.toString());
        }

        // Generar imagen
        generarImagen();
    }

    public static void generarImagen() throws IOException {
        File dotFile = new File("src/main/resources/static/reporte_vehiculos.dot");
        if (dotFile.exists()) {
            String command = "dot -Tpng -Gdpi=200 -Gsize=10,5! src/main/resources/static/reporte_vehiculos.dot -o src/main/resources/static/reporte_vehiculos.png";
            Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                System.out.println("✅ Imagen de vehículos generada");
            } catch (InterruptedException e) {
                System.err.println("❌ Error al generar imagen: " + e.getMessage());
            }
        }
    }
}
