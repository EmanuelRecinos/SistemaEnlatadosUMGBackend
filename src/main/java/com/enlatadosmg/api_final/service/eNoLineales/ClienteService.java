package com.enlatadosmg.api_final.service.eNoLineales;

import com.enlatadosmg.api_final.model.Cliente;
import com.enlatadosmg.api_final.util.CsvLoader;
import com.enlatadosmg.api_final.util.ReporteCliente;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final ArbolAVL arbolClientes = new ArbolAVL();

    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        arbolClientes.recorridoInorden(clientes);
        return clientes;
    }

    public Cliente obtenerPorCui(long cui) {
        return arbolClientes.buscar(cui);
    }

    public void agregarCliente(Cliente cliente) throws IOException {
        arbolClientes.insertar(cliente);
        ReporteCliente.generarReporte(arbolClientes);
    }

    public boolean actualizarCliente(long cui, Cliente nuevo) throws IOException {
        Cliente existente = arbolClientes.buscar(cui);
        if (existente != null) {
            existente.setNombre(nuevo.getNombre());
            existente.setApellido(nuevo.getApellido());
            existente.setTelefono(nuevo.getTelefono());
            existente.setDireccion(nuevo.getDireccion());
            ReporteCliente.generarReporte(arbolClientes);
            return true;
        }
        return false;
    }

    public boolean eliminarCliente(long cui) throws IOException {
        boolean eliminado = arbolClientes.eliminar(cui);
        if (eliminado) {
            ReporteCliente.generarReporte(arbolClientes);
        }
        return eliminado;
    }

    public void cargarDesdeCsv(String path) throws IOException {
        List<Cliente> clientes = CsvLoader.cargarClientesDesdeResource(path);
        for (Cliente c : clientes) {
            arbolClientes.insertar(c);
        }
        ReporteCliente.generarReporte(arbolClientes);
    }

    public ArbolAVL getArbolClientes() {
        return arbolClientes;
    }

    @PostConstruct
    public void init() {
        try {
            cargarDesdeCsv("clientes.csv");
            System.out.println("✅ Clientes cargados automáticamente desde CSV al iniciar la API");
        } catch (IOException e) {
            System.err.println("❌ Error al cargar clientes desde CSV en el inicio: " + e.getMessage());
        }
    }
}
