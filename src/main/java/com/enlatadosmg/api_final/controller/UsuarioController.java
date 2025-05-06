package com.enlatadosmg.api_final.controller;

import com.enlatadosmg.api_final.model.Usuario;
import com.enlatadosmg.api_final.service.eLineales.UsuarioService;
import com.enlatadosmg.api_final.util.CsvLoader;
import com.enlatadosmg.api_final.util.ReporteUsuarios;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReporteUsuarios reporteUsuarios;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<String> iniciarSesion(@RequestBody Usuario usuario) {
        try {
            String mensaje = usuarioService.autenticarUsuario(usuario.getId(), usuario.getContraseña());
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al iniciar sesión: " + e.getMessage());
        }
    }

    @PostMapping("/cerrar-sesion")
    public String cerrarSesion() {
        return usuarioService.cerrarSesion();
    }

    @GetMapping("/perfil")
    public String obtenerPerfil() {
        return usuarioService.obtenerPerfil();
    }

    @PostMapping("/agregar")
    public String agregarUsuario(@RequestBody Usuario usuario) {
        usuarioService.agregarUsuario(usuario); // Agrega el usuario a la lista

        // Después de agregar el usuario, generamos el reporte
        try {
            // Generamos el reporte después de agregar el usuario
            ReporteUsuarios.generarReporte(usuarioService.getListaUsuarios());
            ReporteUsuarios.generarImagen(); // Generamos la imagen del reporte
        } catch (IOException e) {
            return "Error al generar el reporte: " + e.getMessage();
        }

        return "Usuario agregado correctamente y reporte generado.";
    }

    @PostMapping("/cargar-csv")
    public String cargarUsuariosDesdeCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "El archivo CSV está vacío.";
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length != 4) {
                    continue;
                }
                Long id = Long.parseLong(datos[0]);
                String nombre = datos[1];
                String apellidos = datos[2];
                String contraseña = datos[3];
                usuarioService.agregarUsuario(new Usuario(id, nombre, apellidos, contraseña));
            }
            return "Usuarios cargados correctamente.";
        } catch (Exception e) {
            return "Error al procesar el archivo CSV: " + e.getMessage();
        }
    }

    @PostConstruct
    public void cargarUsuariosAutomaticamente() {
        List<Usuario> usuarios = CsvLoader.cargarUsuariosDesdeResource("usuarios.csv");
        for (Usuario usuario : usuarios) {
            usuarioService.agregarUsuario(usuario);
        }
        System.out.println("✅ Usuarios cargados automáticamente al iniciar: " + usuarios.size());

        // Generamos el reporte al iniciar
        try {
            ReporteUsuarios.generarReporte(usuarioService.getListaUsuarios());
            ReporteUsuarios.generarImagen(); // Generamos la imagen del reporte
        } catch (IOException e) {
            System.err.println("Error al generar el reporte automático: " + e.getMessage());
        }
    }

    @GetMapping("/todos")
    public String obtenerTodosUsuarios() {
        return usuarioService.obtenerTodosUsuarios();
    }
}
