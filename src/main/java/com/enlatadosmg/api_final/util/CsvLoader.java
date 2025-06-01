package com.enlatadosmg.api_final.util;

import com.enlatadosmg.api_final.model.Cliente;
import com.enlatadosmg.api_final.model.Repartidor;
import com.enlatadosmg.api_final.model.Usuario;
import com.enlatadosmg.api_final.model.Vehiculo;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    public static List<Usuario> cargarUsuariosDesdeResource(String filename) {
        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    Long id = Long.parseLong(datos[0]);
                    String nombre = datos[1];
                    String apellidos = datos[2];
                    String contraseña = datos[3];
                    usuarios.add(new Usuario(id, nombre, apellidos, contraseña));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error al cargar usuarios desde CSV: " + e.getMessage());
        }

        return usuarios;
    }

    public static List<Repartidor> cargarRepartidoresDesdeResource(String filename) {
        List<Repartidor> repartidores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    long cui = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    String apellidos = datos[2].trim();
                    String licencia = datos[3].trim();
                    String telefono = datos[4].trim();
                    repartidores.add(new Repartidor(cui, nombre, apellidos, licencia, telefono));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error al cargar repartidores desde CSV: " + e.getMessage());
        }

        return repartidores;
    }
    public static List<Vehiculo> cargarVehiculosDesdeResource(String filename) {
        List<Vehiculo> vehiculos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    String placa = datos[0].trim();
                    String marca = datos[1].trim();
                    String modelo = datos[2].trim();
                    String color = datos[3].trim();
                    int anio = Integer.parseInt(datos[4].trim());

                    vehiculos.add(new Vehiculo(placa, marca, modelo, color, anio));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error al cargar vehículos desde CSV: " + e.getMessage());
        }

        return vehiculos;
    }
    public static List<Cliente> cargarClientesDesdeResource(String filename) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    long cui = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    String apellidos = datos[2].trim();
                    String telefono = datos[3].trim();
                    String direccion = datos[4].trim();

                    clientes.add(new Cliente(cui, nombre, apellidos, telefono, direccion));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error al cargar clientes desde CSV: " + e.getMessage());
        }

        return clientes;
    }


}
