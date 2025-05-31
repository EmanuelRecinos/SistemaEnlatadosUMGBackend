package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Usuario;
import com.enlatadosmg.api_final.service.eLineales.Lista;
import com.enlatadosmg.api_final.service.eLineales.Nodo;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class ReporteUsuarios {

    // Método para generar el archivo .dot con la lista de usuarios desde UsuarioService
    public static void generarReporte(Lista listaUsuarios) throws IOException {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph G {\n");
        dotContent.append("node [shape=record];\n");

        Nodo nodo = listaUsuarios.getIni();
        while (nodo != null) {
            Usuario usuario = (Usuario) nodo.data;
            dotContent.append("    Usuario").append(usuario.getId()).append(" [label=\"")
                    .append("ID: ").append(usuario.getId()).append("\\n")
                    .append("Nombre: ").append(usuario.getNombre()).append("\\n")
                    .append("Apellidos: ").append(usuario.getApellidos()).append("\\n")
                    .append("Contraseña: ").append(usuario.getContraseña()).append("\"];\n");

            if (nodo.sig != null) {
                Usuario siguienteUsuario = (Usuario) nodo.sig.data;
                dotContent.append("    Usuario").append(usuario.getId()).append(" -> Usuario")
                        .append(siguienteUsuario.getId()).append(";\n");
            }

            nodo = nodo.sig;
        }
        dotContent.append("}\n");

        // Guardamos el archivo .dot en la carpeta 'static/'
        File file = new File("src/main/resources/static/reporte_usuarios.dot");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(dotContent.toString());
        }

        System.out.println("Reporte generado: src/main/resources/static/reporte_usuarios.dot");
    }

    // Método para generar la imagen del gráfico usando Graphviz
    public static void generarImagen() throws IOException {
        // Ejecutar Graphviz para convertir el archivo .dot a una imagen
        File dotFile = new File("src/main/resources/static/reporte_usuarios.dot");
        if (dotFile.exists()) {
            String command = "dot -Tpng src/main/resources/static/reporte_usuarios.dot -o src/main/resources/static/reporte_usuarios.png";
            Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                System.out.println("Imagen generada: src/main/resources/static/reporte_usuarios.png");
            } catch (InterruptedException e) {
                System.err.println("Error al generar la imagen: " + e.getMessage());
            }
        } else {
            System.err.println("No se encontró el archivo .dot");
        }
    }
}
