package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;

public class Pila extends Lista {
    //Utilizamos constructor de Lista para asignar nombre
    public Pila() {
        super("Mi Pila");
    }

    // Agrega Modelo Matematico a la pila
    public void push(Object objecto) {

        insertarAlFrente(objecto);
    }

    //Elimina Modelo Matematico de la Pila

    public Object pop() throws ExcepcionListaVacia {
        return removerDelFrente();
    }
}