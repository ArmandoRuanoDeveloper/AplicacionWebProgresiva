package com.proyecto.servicios.service;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.model.onboarding.ClienteActualizacionRequest;
import com.proyecto.servicios.model.onboarding.ClienteRegistroRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ClienteService {
    Cliente registrarCliente(ClienteRegistroRequest request);
    Cliente obtenerPorId(Integer id);
    Cliente obtenerPorCurp(String curp);
    Cliente obtenerPorRfc(String rfc);
    Cliente obtenerPorCorreo(String correo);
    List<Cliente> listarTodos();
    List<Cliente> listarActivos();
    List<Cliente> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta);
    Cliente actualizarCliente(Integer id, ClienteActualizacionRequest request);
    void darDeBajaCliente(Integer id);
}