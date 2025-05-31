    package com.enlatadosmg.api_final.service.eLineales;
    
    import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;
    @Setter
    @Getter
    @Data
    
    public class Lista {
        private Nodo ini, fin; //agregamos el primer atributo
    
        private String nombre; //agregamos el segundo atributo
    
        //agregamos
        public Lista(String nombre) {
    
            ini = fin = null;
            //this.nombre= nombre; en lugar de eso lo de bajo y poner el import arriba
            setNombre(nombre);
    
        }
    
        public Lista() {
            this("La Primera"); // Asignamos nombre a la lista
        }
    
        public boolean estaVacia() {
            return null == ini;
            // esta es la simplificaccion de un if para verificar si la lista esta vacia
        }
    
        // se coloca Object d porque acepta cualquier tipo de dato y este se almacena en la clase nodo en el constructor
        public void insertarAlFrente(Object d) {
    
            if (estaVacia())
    
                ini = fin = new Nodo(d); //ini = fin apunta al nuevo modelo matematico (Nodo)
    
            else
                ini = new Nodo(d, ini);
        }
    
        // se coloca Object d porque acepta cualquier tipo de dato y este se almacena en la clase nodo en el constructor
        public void insertarAlFinal(Object d) {
    
            if (estaVacia())
    
                ini = fin = new Nodo(d);
            else
                fin = fin.sig = new Nodo(d);
    
    
        }
    
        ;
    
        public Object removerAlFinal() throws ExcepcionListaVacia {
    //verifica si la lista esta vacia y lanza excepcion para arreglar el error
            if (estaVacia())
                throw new ExcepcionListaVacia(nombre);
    
            Object elementoEliminado = fin.data; // Guarda el dato del ultimo nodo antes de eliminarlo
    
            if (ini == fin)
                ini = fin = null;
            else {
    
                Nodo actual = ini;
    
                while (actual.sig != fin)
                    actual = actual.sig;
    
                fin = actual;
                actual.sig = null;
    
            }
            return elementoEliminado;
        }
    
        public Object removerDelFrente() throws ExcepcionListaVacia {
    
    
            //verifica si la lista esta vacia y lanza excepcion para arreglar el error
            if (estaVacia())
                throw new ExcepcionListaVacia(nombre);
            Object elementoEliminado = ini.data;
    
            if (ini == fin)
                ini = fin = null;
    
            else
    
                ini = ini.sig;
    
            return elementoEliminado;
        }
        public String imprimir(){
    
            StringBuilder str = new StringBuilder();
    
            if(estaVacia())
                return String.format("%s vacia\n",nombre);
    
            Nodo actual = ini;
            str.append(String.format("La %s es: ", nombre));
    
            while (actual != null){
    
                str.append(String.format("%s",actual.data));
                actual = actual.sig;
            }
            return str.toString().trim();
        }
        @Override
        public String toString(){
    
            return "Lista{"+
                    imprimir()+
                    '}';
    
        }
        public int contar() {
            int contador = 0;
            Nodo actual = ini;
    
            while (actual != null) {
                contador++;
                actual = actual.getSig();
            }
    
            return contador;
        }
    
    }// cierre de la clase
    
