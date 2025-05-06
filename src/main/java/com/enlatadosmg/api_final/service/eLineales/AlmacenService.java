package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.Caja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlmacenService {

    private static final Logger logger = LoggerFactory.getLogger(AlmacenService.class);


    private final Pila pila = new Pila();
    private int contadorCorrelativo = 0;

    public void agregarCaja() {

        Caja nuevaCaja = new Caja(++contadorCorrelativo, LocalDate.now());

        pila.push(nuevaCaja);

        logger.info("Caja agregada: Correlativo = {}, Fecha de ingreso = {}", nuevaCaja.getCorrelativo(), nuevaCaja.getFechaIngreso());
    }

    public Caja retirarCaja() {
        try {
            return (Caja) pila.pop();
        } catch (ExcepcionListaVacia e) {
            return null;
        }
    }

    public List<Caja> listarCajas() {
        List<Caja> cajas = new ArrayList<>();
        Nodo actual = pila.getIni(); // Acceso al nodo inicial de la pila
        while (actual != null) {
            cajas.add((Caja) actual.getData());
            actual = actual.getSig();
        }
        return cajas;
    }
    public void cargarCajasIniciales(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            agregarCaja();
        }
    }

}
