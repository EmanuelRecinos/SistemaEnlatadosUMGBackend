package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Repartidor;
import com.enlatadosmg.api_final.service.eLineales.Nodo;
import com.enlatadosmg.api_final.service.eLineales.Lista;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReporteRepartidor {

    public static void generarReporte(Lista lista) throws IOException {
        StringBuilder dot = new StringBuilder("digraph G {\n");
        dot.append("rankdir=LR;\n");
        dot.append("size=\"10,5!\";\n");      // Tamaño fijo del gráfico en pulgadas
        dot.append("dpi=200;\n");             // Resolución 200 dpi
        dot.append("node [shape=record];\n");

        Nodo actual = lista.getIni();
        while (actual != null) {
            Repartidor r = (Repartidor) actual.getData();
            dot.append("R").append(r.getCui()).append("[label=\"{CUI: ").append(r.getCui())
                    .append(" | ").append(r.getNombre()).append(" ").append(r.getApellidos())
                    .append(" | Licencia: ").append(r.getLicencia())
                    .append(" | Tel: ").append(r.getTelefono()).append("}\"];\n");

            if (actual.getSig() != null) {
                Repartidor siguiente = (Repartidor) actual.getSig().getData();
                dot.append("R").append(r.getCui()).append(" -> R").append(siguiente.getCui()).append(";\n");
            }
            actual = actual.getSig();
        }

        dot.append("}\n");

        // Guardar archivo .dot
        File dotFile = new File("src/main/resources/static/reporte_repartidores.dot");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFile))) {
            writer.write(dot.toString());
        }

        // Generar imagen
        generarImagen();
    }

    public static void generarImagen() throws IOException {
        File dotFile = new File("src/main/resources/static/reporte_repartidores.dot");
        if (dotFile.exists()) {

            String command = "dot -Tpng -Gdpi=200 -Gsize=10,5! src/main/resources/static/reporte_repartidores.dot -o src/main/resources/static/reporte_repartidores.png";
            Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                System.out.println("✅ Imagen de repartidores generada");
            } catch (InterruptedException e) {
                System.err.println("❌ Error al generar imagen: " + e.getMessage());
            }
        }
    }
}
