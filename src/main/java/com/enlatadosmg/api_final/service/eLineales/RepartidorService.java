package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.exception.ExcepcionRepartidorNoEncontrado;
import com.enlatadosmg.api_final.model.Repartidor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepartidorService {

    private Cola colaRepartidores;

    public RepartidorService() {
        colaRepartidores = new Cola();
    }

    // Crear repartidor
    public void insertarRepartidor(Repartidor repartidor) {
        colaRepartidores.encolar(repartidor);
    }

    // Modificar repartidor
    public void modificarRepartidor(Repartidor repartidor) {
        boolean encontrado = false;
        Nodo nodoActual = colaRepartidores.getListaCola().getIni(); // Obtenemos el primer nodo

        while (nodoActual != null) {
            Repartidor repartidorEnCola = (Repartidor) nodoActual.getData();
            if (repartidorEnCola.getCui() == repartidor.getCui()) {
                // Si encontramos el repartidor, lo modificamos
                repartidorEnCola.setNombre(repartidor.getNombre());
                repartidorEnCola.setApellidos(repartidor.getApellidos());
                repartidorEnCola.setLicencia(repartidor.getLicencia());
                repartidorEnCola.setTelefono(repartidor.getTelefono());
                encontrado = true;
                break;
            }
            nodoActual = nodoActual.getSig();
        }

        if (!encontrado) {
            throw new ExcepcionRepartidorNoEncontrado("Repartidor con CUI " + repartidor.getCui() + " no encontrado.");
        }
    }

    //Mostrar repartidores
    public List<Repartidor> mostrarRepartidores() {
        List<Repartidor> repartidores = new ArrayList<>();
        Nodo nodoActual = colaRepartidores.getListaCola().getIni(); // Obtener el primer nodo de la cola

        // Recorremos la cola y agregamos cada repartidor a la lista
        while (nodoActual != null) {
            Repartidor repartidor = (Repartidor) nodoActual.getData(); // Obtener el repartidor del nodo
            repartidores.add(repartidor); // Agregarlo a la lista
            nodoActual = nodoActual.getSig(); // Avanzar al siguiente nodo
        }

        return repartidores; // Devolver la lista de repartidores
    }



    // Asignar un repartidor a un pedido (desencolar)
    public Repartidor asignarRepartidor() throws ExcepcionListaVacia {
        return (Repartidor) colaRepartidores.desencolar();
    }

    // Regresar un repartidor a la cola después de completar un pedido (encolar)
    public void regresarRepartidor(Repartidor repartidor) {
        colaRepartidores.encolar(repartidor);
    }

    // Cargar repartidores desde un archivo CSV
    public void cargarRepartidoresDesdeCSV(MultipartFile archivoCsv) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivoCsv.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    long cui = Long.parseLong(datos[0].trim());
                    String nombre = datos[1].trim();
                    String apellido = datos[2].trim();
                    String licencia = datos[3].trim();
                    String telefono = datos[4].trim();
                    Repartidor repartidor = new Repartidor(cui, nombre, apellido, licencia, telefono);
                    insertarRepartidor(repartidor);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error al procesar el archivo CSV: " + e.getMessage());
        }
    }

    // Eliminar repartidor (utilizando el CUI)
    public void eliminarRepartidor(long cui) {
        Cola colaTemporal = new Cola();
        boolean eliminado = false;

        while (!colaRepartidores.estaVacia()) {
            try {
                Repartidor actual = (Repartidor) colaRepartidores.desencolar();
                if (actual.getCui() == cui) {
                    eliminado = true; // Encontrado y eliminado (no se encola)
                } else {
                    colaTemporal.encolar(actual); // Mantener el orden
                }
            } catch (ExcepcionListaVacia e) {
                break; // Por si ocurre una excepción inesperada
            }
        }

        // Restaurar los elementos a la cola original
        while (!colaTemporal.estaVacia()) {
            try {
                colaRepartidores.encolar(colaTemporal.desencolar());
            } catch (ExcepcionListaVacia e) {
                break;
            }
        }

        if (!eliminado) {
            throw new ExcepcionRepartidorNoEncontrado("Repartidor con CUI " + cui + " no encontrado.");
        }
    }


}
