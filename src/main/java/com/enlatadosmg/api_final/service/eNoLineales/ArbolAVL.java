package com.enlatadosmg.api_final.service.eNoLineales;

import com.enlatadosmg.api_final.model.Cliente;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class ArbolAVL {
    private NodoAVL raiz;
    private boolean eliminado = false;


    public void insertar(Cliente cliente) {
        raiz = insertarRecursivo(raiz, cliente);
    }

    private NodoAVL insertarRecursivo(NodoAVL nodo, Cliente cliente) {
        if (nodo == null) return new NodoAVL(cliente);

        if (cliente.getCui() < nodo.getCliente().getCui()) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), cliente));
        } else if (cliente.getCui() > nodo.getCliente().getCui()) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), cliente));
        } else {
            return nodo; // CUI ya existe
        }

        nodo.setAltura(1 + Math.max(obtenerAltura(nodo.getIzquierdo()), obtenerAltura(nodo.getDerecho())));
        return balancear(nodo);
    }


    public Cliente buscar(long cui) {
        NodoAVL resultado = buscarRecursivo(raiz, cui);
        return resultado != null ? resultado.getCliente() : null;
    }

    private NodoAVL buscarRecursivo(NodoAVL nodo, long cui) {
        if (nodo == null || nodo.getCliente().getCui() == cui) return nodo;
        return cui < nodo.getCliente().getCui()
                ? buscarRecursivo(nodo.getIzquierdo(), cui)
                : buscarRecursivo(nodo.getDerecho(), cui);
    }


    public boolean eliminar(long cui) {
        eliminado = false; // reinicia el flag antes de eliminar
        raiz = eliminarRecursivo(raiz, cui);
        return eliminado;
    }

    private NodoAVL eliminarRecursivo(NodoAVL nodo, long cui) {
        if (nodo == null) return null;

        if (cui < nodo.getCliente().getCui()) {
            nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), cui));
        } else if (cui > nodo.getCliente().getCui()) {
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), cui));
        } else {
            eliminado = true; // Nodo encontrado y será eliminado
            if (nodo.getIzquierdo() == null || nodo.getDerecho() == null) {
                nodo = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo() : nodo.getDerecho();
            } else {
                NodoAVL sucesor = obtenerMinimo(nodo.getDerecho());
                nodo.setCliente(sucesor.getCliente());
                nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), sucesor.getCliente().getCui()));
            }
        }

        if (nodo == null) return null;

        nodo.setAltura(1 + Math.max(obtenerAltura(nodo.getIzquierdo()), obtenerAltura(nodo.getDerecho())));
        return balancear(nodo);
    }

    private NodoAVL obtenerMinimo(NodoAVL nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo;
    }


    public void modificar(Cliente cliente) {
        NodoAVL nodo = buscarRecursivo(raiz, cliente.getCui());
        if (nodo != null) {
            nodo.getCliente().setNombre(cliente.getNombre());
            nodo.getCliente().setApellido(cliente.getApellido());
            nodo.getCliente().setTelefono(cliente.getTelefono());
            nodo.getCliente().setDireccion(cliente.getDireccion());
        }
    }


    public void recorridoInorden() {
        recorridoInordenRecursivo(raiz);
    }

    private void recorridoInordenRecursivo(NodoAVL nodo) {
        if (nodo != null) {
            recorridoInordenRecursivo(nodo.getIzquierdo());
            System.out.println(nodo.getCliente());
            recorridoInordenRecursivo(nodo.getDerecho());
        }
    }


    private int obtenerAltura(NodoAVL nodo) {
        return nodo == null ? 0 : nodo.getAltura();
    }

    private int obtenerBalance(NodoAVL nodo) {
        return nodo == null ? 0 : obtenerAltura(nodo.getIzquierdo()) - obtenerAltura(nodo.getDerecho());
    }

    private NodoAVL balancear(NodoAVL nodo) {
        int balance = obtenerBalance(nodo);

        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0)
            return rotarDerecha(nodo);

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0)
            return rotarIzquierda(nodo);

        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private NodoAVL rotarDerecha(NodoAVL nodo) {
        NodoAVL nuevaRaiz = nodo.getIzquierdo();
        NodoAVL temp = nuevaRaiz.getDerecho();
        nuevaRaiz.setDerecho(nodo);
        nodo.setIzquierdo(temp);

        nodo.setAltura(Math.max(obtenerAltura(nodo.getIzquierdo()), obtenerAltura(nodo.getDerecho())) + 1);
        nuevaRaiz.setAltura(Math.max(obtenerAltura(nuevaRaiz.getIzquierdo()), obtenerAltura(nuevaRaiz.getDerecho())) + 1);

        return nuevaRaiz;
    }

    private NodoAVL rotarIzquierda(NodoAVL nodo) {
        NodoAVL nuevaRaiz = nodo.getDerecho();
        NodoAVL temp = nuevaRaiz.getIzquierdo();
        nuevaRaiz.setIzquierdo(nodo);
        nodo.setDerecho(temp);

        nodo.setAltura(Math.max(obtenerAltura(nodo.getIzquierdo()), obtenerAltura(nodo.getDerecho())) + 1);
        nuevaRaiz.setAltura(Math.max(obtenerAltura(nuevaRaiz.getIzquierdo()), obtenerAltura(nuevaRaiz.getDerecho())) + 1);

        return nuevaRaiz;
    }

    public void recorridoInorden(List<Cliente> clientes) {
        if (raiz != null) {
            raiz.recorridoInorden(clientes);
        }
    }
}
