package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import org.springframework.stereotype.Component;

@Component
public class Cola {

    private Lista listaCola;

    //contructor

    public Cola(){

        listaCola = new Lista("Mi Cola");

    }

    public void encolar(Object objeto){

        listaCola.insertarAlFinal(objeto);
    }

    public Object desencolar () throws ExcepcionListaVacia {

        return listaCola.removerDelFrente();
    }

    public boolean estaVacia (){

        return listaCola.estaVacia();
    }


    public void imprimir()
    {
        listaCola.imprimir();

    }
    public Lista getListaCola() {
        return listaCola;
    }

}
