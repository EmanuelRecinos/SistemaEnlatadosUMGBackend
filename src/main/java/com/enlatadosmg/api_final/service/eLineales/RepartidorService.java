package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.Repartidor;
import com.enlatadosmg.api_final.service.eLineales.Cola;
import com.enlatadosmg.api_final.service.eLineales.Nodo;
import com.enlatadosmg.api_final.service.eLineales.Lista;
import com.enlatadosmg.api_final.util.CsvLoader;
import com.enlatadosmg.api_final.util.ReporteRepartidor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepartidorService {

    private final Cola cola = new Cola();

    public List<Repartidor> obtenerTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            repartidores.add((Repartidor) actual.getData());
            actual = actual.getSig();
        }
        return repartidores;
    }

    public Repartidor obtenerPorCui(long cui) {
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            Repartidor r = (Repartidor) actual.getData();
            if (r.getCui() == cui) {
                return r;
            }
            actual = actual.getSig();
        }
        return null;
    }

    public void agregarRepartidor(Repartidor repartidor) throws IOException {
        cola.encolar(repartidor);
        ReporteRepartidor.generarReporte(cola.getListaCola());
    }

    public boolean actualizarRepartidor(long cui, Repartidor nuevo) throws IOException {
        Nodo actual = cola.getListaCola().getIni();
        while (actual != null) {
            Repartidor r = (Repartidor) actual.getData();
            if (r.getCui() == cui) {
                r.setNombre(nuevo.getNombre());
                r.setApellidos(nuevo.getApellidos());
                r.setLicencia(nuevo.getLicencia());
                r.setTelefono(nuevo.getTelefono());
                ReporteRepartidor.generarReporte(cola.getListaCola());
                return true;
            }
            actual = actual.getSig();
        }
        return false;
    }

    public boolean eliminarRepartidor(long cui) throws IOException {
        Nodo anterior = null;
        Nodo actual = cola.getListaCola().getIni();

        while (actual != null) {
            Repartidor r = (Repartidor) actual.getData();
            if (r.getCui() == cui) {
                if (anterior == null) {
                    cola.getListaCola().setIni(actual.getSig());
                } else {
                    anterior.setSig(actual.getSig());
                }
                ReporteRepartidor.generarReporte(cola.getListaCola());
                return true;
            }
            anterior = actual;
            actual = actual.getSig();
        }

        return false;
    }

    public Repartidor asignarRepartidor() throws ExcepcionListaVacia, IOException {
        Repartidor r = (Repartidor) cola.desencolar();
        ReporteRepartidor.generarReporte(cola.getListaCola());
        return r;
    }

    public void devolverRepartidor(Repartidor repartidor) throws IOException {
        cola.encolar(repartidor);
        ReporteRepartidor.generarReporte(cola.getListaCola());
    }

    public void cargarDesdeCsv(String path) throws IOException {
        List<Repartidor> repartidores = CsvLoader.cargarRepartidoresDesdeResource(path);
        for (Repartidor r : repartidores) {
            cola.encolar(r);
        }
        ReporteRepartidor.generarReporte(cola.getListaCola());
    }

    public Lista obtenerListaInterna() {
        return cola.getListaCola();
    }

    @PostConstruct
    public void init() {
        try {
            cargarDesdeCsv("repartidores.csv");
            System.out.println("✅ Repartidores cargados automáticamente desde CSV al iniciar la API");
        } catch (IOException e) {
            System.err.println("❌ Error al cargar repartidores desde CSV en el inicio: " + e.getMessage());
        }
    }
}
