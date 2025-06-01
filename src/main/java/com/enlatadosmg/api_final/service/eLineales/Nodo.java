package com.enlatadosmg.api_final.service.eLineales;
import lombok.Data;

@Data
public class Nodo {
    public Object data;
    public Nodo sig;

    public Nodo(Object data, Nodo sig){//
        setSig(sig);
        setData(data);

    }
    public Nodo (Object data){ // se usa este constructor cuando se crea el primer nodo
        //ya que se asume que no hay nodo siguiente

        this(data,null);
    }
}

