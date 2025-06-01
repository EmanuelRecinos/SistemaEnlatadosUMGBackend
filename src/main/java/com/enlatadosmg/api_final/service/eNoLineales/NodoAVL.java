package com.enlatadosmg.api_final.service.eNoLineales;

import com.enlatadosmg.api_final.model.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NodoAVL {
    private Cliente cliente;
    private NodoAVL izquierdo;
    private NodoAVL derecho;
    private int altura;

    public NodoAVL(Cliente cliente) {
        this.cliente = cliente;
        this.altura = 1; // Un nuevo nodo se crea con altura 1
    }

    // Método recursivo inorden que agrega los clientes a una lista
    public void recorridoInorden(List<Cliente> clientes) {
        if (izquierdo != null) {
            izquierdo.recorridoInorden(clientes); // Recorremos el subárbol izquierdo
        }

        // Agregamos el cliente del nodo actual a la lista
        clientes.add(cliente);

        if (derecho != null) {
            derecho.recorridoInorden(clientes); // Recorremos el subárbol derecho
        }
    }
}
