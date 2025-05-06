package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Usuario;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {
    public static List<Usuario> cargarUsuariosDesdeResource(String filename) {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            // Buscar el archivo dentro de src/main/resources
            ClassPathResource resource = new ClassPathResource(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    Long id = Long.parseLong(datos[0]);
                    String nombre = datos[1];
                    String apellidos = datos[2];
                    String contraseña = datos[3];

                    Usuario usuario = new Usuario(id, nombre, apellidos, contraseña);
                    usuarios.add(usuario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error al cargar usuarios desde el recurso CSV: " + e.getMessage());
        }

        return usuarios;
    }
}
