package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Cliente;
import com.enlatadosmg.api_final.service.eNoLineales.ArbolAVL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReporteCliente {

    public static void generarReporte(ArbolAVL arbol) throws IOException {
        StringBuilder dot = new StringBuilder("digraph G {\n");
        dot.append("node [shape=record, style=filled, fillcolor=lightblue];\n");

        List<Cliente> clientes = new ArrayList<>();
        arbol.recorridoInorden(clientes);

        for (Cliente c : clientes) {
            dot.append("C").append(c.getCui()).append("[label=\"{CUI: ").append(c.getCui())
                    .append(" | Nombre: ").append(c.getNombre())
                    .append(" | Apellido: ").append(c.getApellido())
                    .append(" | Tel: ").append(c.getTelefono())
                    .append(" | Dir: ").append(c.getDireccion()).append("}\"];\n");
        }

        // Puedes añadir relaciones si deseas mostrar la estructura del árbol.
        agregarConexiones(dot, arbol.getRaiz());

        dot.append("}\n");

        File dotFile = new File("src/main/resources/static/reporte_clientes.dot");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFile))) {
            writer.write(dot.toString());
        }

        generarImagen();
    }

    private static void agregarConexiones(StringBuilder dot, com.enlatadosmg.api_final.service.eNoLineales.NodoAVL nodo) {
        if (nodo == null) return;

        if (nodo.getIzquierdo() != null) {
            dot.append("C").append(nodo.getCliente().getCui())
                    .append(" -> C").append(nodo.getIzquierdo().getCliente().getCui()).append(";\n");
            agregarConexiones(dot, nodo.getIzquierdo());
        }

        if (nodo.getDerecho() != null) {
            dot.append("C").append(nodo.getCliente().getCui())
                    .append(" -> C").append(nodo.getDerecho().getCliente().getCui()).append(";\n");
            agregarConexiones(dot, nodo.getDerecho());
        }
    }

    public static void generarImagen() throws IOException {
        File dotFile = new File("src/main/resources/static/reporte_clientes.dot");
        if (dotFile.exists()) {
            String command = "dot -Tpng src/main/resources/static/reporte_clientes.dot -o src/main/resources/static/reporte_clientes.png";
            Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                System.out.println("✅ Imagen de clientes generada correctamente.");
            } catch (InterruptedException e) {
                System.err.println("❌ Error al generar la imagen del reporte de clientes: " + e.getMessage());
            }
        }
    }
}
