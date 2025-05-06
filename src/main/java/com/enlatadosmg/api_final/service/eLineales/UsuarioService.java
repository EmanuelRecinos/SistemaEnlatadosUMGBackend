package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.model.Usuario;
import com.enlatadosmg.api_final.util.ReporteUsuarios;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UsuarioService {

    private final Lista listaUsuarios; // Lista enlazada de usuarios
    private Usuario usuarioSesionActual; // Para almacenar el usuario de la sesión actual

    public UsuarioService() {
        listaUsuarios = new Lista("Usuarios"); // Inicializamos la lista enlazada con el nombre "Usuarios"
    }

    // Método para obtener la lista de usuarios
    public Lista getListaUsuarios() {
        return listaUsuarios;
    }

    // Método para agregar un usuario
    public void agregarUsuario(Usuario usuario) {
        listaUsuarios.insertarAlFinal(usuario); // Insertamos el usuario al final de la lista
        try {
            // Generamos el reporte después de agregar el usuario
            ReporteUsuarios.generarReporte(listaUsuarios);  // Generamos el archivo .dot
            ReporteUsuarios.generarImagen();  // Generamos la imagen .png desde el .dot
        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    // Método para obtener un usuario por su ID
    public Usuario obtenerUsuarioPorId(Long id) {
        Nodo nodo = listaUsuarios.getIni(); // Obtenemos el primer nodo
        while (nodo != null) {
            Usuario usuario = (Usuario) nodo.data; // nodo.data toma el Objeto del nodo actual y lo convierte del a tipo Usuario con Casting
            if (usuario.getId().equals(id)) {
                return usuario; // Retornamos el usuario si encontramos el ID
            }
            nodo = nodo.sig; // Nos movemos al siguiente nodo
        }
        return null; // Si no encontramos el usuario, retornamos null
    }

    // Método para autenticar un usuario por ID y contraseña
    public String autenticarUsuario(Long id, String contraseña) {
        Usuario usuario = obtenerUsuarioPorId(id);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            usuarioSesionActual = usuario; // Establecemos el usuario en la sesión actual
            return "Sesión iniciada exitosamente.";
        }
        return "Credenciales incorrectas.";
    }

    // Método para cerrar la sesión
    public String cerrarSesion() {
        if (usuarioSesionActual != null) {
            usuarioSesionActual = null; // Limpiamos la sesión
            return "Sesión cerrada exitosamente.";
        }
        return "No hay sesión activa.";
    }

    // Método para obtener el perfil del usuario autenticado
    public String obtenerPerfil() {
        if (usuarioSesionActual != null) {
            return "Perfil de usuario: " + usuarioSesionActual.getNombre() + " " + usuarioSesionActual.getApellidos();
        }
        return "No hay sesión activa. Inicie sesión primero.";
    }

    public String obtenerTodosUsuarios() {
        if (listaUsuarios.estaVacia()) {
            return "No hay usuarios.";
        }

        Nodo nodo = listaUsuarios.getIni();
        StringBuilder resultado = new StringBuilder();
        while (nodo != null) {
            Usuario usuario = (Usuario) nodo.data;
            System.out.println("Usuario encontrado: " + usuario.getId()); // Agrega esto para depurar
            resultado.append("ID: ").append(usuario.getId())
                    .append(", Nombre: ").append(usuario.getNombre())
                    .append(", Apellidos: ").append(usuario.getApellidos())
                    .append(", Contraseña: ").append(usuario.getContraseña())
                    .append("\n");
            nodo = nodo.sig;
        }
        return resultado.toString();
    }
}

