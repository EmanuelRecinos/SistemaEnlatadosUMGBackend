package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Caja;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteAlmacen {

    public static String generarReporte(List<Caja> cajas) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph Almacen {\n");
        dot.append("rankdir=LR;\n");
        dot.append("node [shape=box, style=filled, color=lightblue];\n");

        for (int i = 0; i < cajas.size(); i++) {
            Caja caja = cajas.get(i);
            dot.append(String.format("caja%d [label=\"Caja #%d\\nFecha: %s\"];\n",
                    i, caja.getCorrelativo(), caja.getFechaIngreso()));
            if (i < cajas.size() - 1) {
                dot.append(String.format("caja%d -> caja%d;\n", i, i + 1));
            }
        }

        dot.append("}");

        try {
            // Crear carpeta 'archivos' si no existe
            File carpeta = new File("archivos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // Definir rutas dentro de la carpeta 'archivos'
            String rutaDot = "archivos/almacen.dot";
            String rutaImg = "archivos/reporte_almacen.png";

            // Guardar archivo .dot
            FileWriter writer = new FileWriter(rutaDot);
            writer.write(dot.toString());
            writer.close();

            // Ejecutar Graphviz para generar la imagen
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", rutaDot, "-o", rutaImg);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "Error generando imagen: Graphviz falló con código " + exitCode;
            }

            // Retornar una URL que luego servirá un endpoint dedicado
            long timestamp = System.currentTimeMillis();
            return "/api/almacen/imagen?t=" + timestamp;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error al generar el reporte: " + e.getMessage();
        }
    }
}
