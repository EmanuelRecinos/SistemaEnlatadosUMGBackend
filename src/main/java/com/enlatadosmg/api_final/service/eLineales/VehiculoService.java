package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.Vehiculo;
import com.enlatadosmg.api_final.util.CsvLoader;
import com.enlatadosmg.api_final.util.ReporteVehiculo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehiculoService {

    private final Cola cola = new Cola();

    public List<Vehiculo> obtenerTodos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            vehiculos.add((Vehiculo) actual.getData());
            actual = actual.getSig();
        }
        return vehiculos;
    }

    public Vehiculo obtenerPorPlaca(String placa) {
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            Vehiculo v = (Vehiculo) actual.getData();
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
            actual = actual.getSig();
        }
        return null;
    }

    public void agregarVehiculo(Vehiculo vehiculo) throws IOException {
        cola.encolar(vehiculo);
        ReporteVehiculo.generarReporte(cola.getListaCola());
    }

    public boolean actualizarVehiculo(String placa, Vehiculo nuevo) throws IOException {
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            Vehiculo v = (Vehiculo) actual.getData();
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                v.setMarca(nuevo.getMarca());
                v.setModelo(nuevo.getModelo());
                v.setAnio(nuevo.getAnio());
                ReporteVehiculo.generarReporte(cola.getListaCola());
                return true;
            }
            actual = actual.getSig();
        }
        return false;
    }

    public boolean eliminarVehiculo(String placa) throws IOException {
        Nodo anterior = null;
        Nodo actual = cola.getListaCola().getIni();

        while (actual != null) {
            Vehiculo v = (Vehiculo) actual.getData();
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                if (anterior == null) {
                    cola.getListaCola().setIni(actual.getSig());
                } else {
                    anterior.setSig(actual.getSig());
                }
                ReporteVehiculo.generarReporte(cola.getListaCola());
                return true;
            }
            anterior = actual;
            actual = actual.getSig();
        }

        return false;
    }

    public void cargarDesdeCsv(String path) throws IOException {
        List<Vehiculo> vehiculos = CsvLoader.cargarVehiculosDesdeResource(path);
        for (Vehiculo v : vehiculos) {
            cola.encolar(v);
        }
        ReporteVehiculo.generarReporte(cola.getListaCola());
    }

    public Lista obtenerListaInterna() {
        return cola.getListaCola();
    }

    @PostConstruct
    public void init() {
        try {
            cargarDesdeCsv("vehiculos.csv");
            System.out.println("✅ Vehículos cargados automáticamente desde CSV al iniciar la API");
        } catch (IOException e) {
            System.err.println("❌ Error al cargar vehículos desde CSV en el inicio: " + e.getMessage());
        }
    }

    // Asigna (quita) el primer vehículo de la cola
    public Vehiculo asignarVehiculo() throws ExcepcionListaVacia, IOException {
        Vehiculo vehiculo = (Vehiculo) cola.desencolar();
        ReporteVehiculo.generarReporte(cola.getListaCola());
        return vehiculo;
    }

    // Devuelve (recoloca) el vehículo al final de la cola
    public void devolverVehiculo(Vehiculo vehiculo) throws IOException {
        cola.encolar(vehiculo);
        ReporteVehiculo.generarReporte(cola.getListaCola());
    }

}
