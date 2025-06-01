package com.enlatadosmg.api_final.service.eLineales;

import com.enlatadosmg.api_final.exception.ExcepcionListaVacia;
import com.enlatadosmg.api_final.model.*;
import com.enlatadosmg.api_final.service.eLineales.*;
import com.enlatadosmg.api_final.service.eNoLineales.ClienteService;
import com.enlatadosmg.api_final.service.eLineales.AlmacenService;
import com.enlatadosmg.api_final.service.eLineales.RepartidorService;
import com.enlatadosmg.api_final.service.eLineales.VehiculoService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final Lista listaPedidos = new Lista("Pedidos");
    private int contadorPedidos = 0;

    private final ClienteService clienteService;
    private final AlmacenService almacenService;
    private final RepartidorService repartidorService;
    private final VehiculoService vehiculoService;

    public PedidoService(
            ClienteService clienteService,
            AlmacenService almacenService,
            RepartidorService repartidorService,
            VehiculoService vehiculoService
    ) {
        this.clienteService = clienteService;
        this.almacenService = almacenService;
        this.repartidorService = repartidorService;
        this.vehiculoService = vehiculoService;
    }

    public Pedido crearPedido(String origen, String destino, long cuiCliente, int cantidadCajas)
            throws ExcepcionListaVacia, IOException {

        Cliente cliente = clienteService.obtenerPorCui(cuiCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        List<Caja> cajas = new ArrayList<>();
        for (int i = 0; i < cantidadCajas; i++) {
            Caja caja = almacenService.retirarCaja();
            if (caja == null) {
                throw new IllegalStateException("No hay suficientes cajas disponibles");
            }
            cajas.add(caja);
        }

        Repartidor repartidor = repartidorService.asignarRepartidor();
        Vehiculo vehiculo = vehiculoService.asignarVehiculo();

        Pedido pedido = new Pedido(
                ++contadorPedidos,
                origen,
                destino,
                LocalDateTime.now(),
                "Pendiente",
                cliente,
                cajas,
                repartidor,
                vehiculo
        );

        listaPedidos.insertarAlFinal(pedido);
        return pedido;
    }

    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        Nodo actual = listaPedidos.getIni();
        while (actual != null) {
            pedidos.add((Pedido) actual.getData());
            actual = actual.getSig();
        }
        return pedidos;
    }

    public Pedido buscarPorNumero(int numero) {
        Nodo actual = listaPedidos.getIni();
        while (actual != null) {
            Pedido pedido = (Pedido) actual.getData();
            if (pedido.getNumeroPedido() == numero) {
                return pedido;
            }
            actual = actual.getSig();
        }
        return null;
    }

    public boolean completarPedido(int numero) throws IOException {
        Nodo actual = listaPedidos.getIni();
        while (actual != null) {
            Pedido pedido = (Pedido) actual.getData();
            if (pedido.getNumeroPedido() == numero && pedido.getEstado().equals("Pendiente")) {
                pedido.setEstado("Completado");
                repartidorService.devolverRepartidor(pedido.getRepartidor());
                vehiculoService.devolverVehiculo(pedido.getVehiculo());
                return true;
            }
            actual = actual.getSig();
        }
        return false;
    }

}
