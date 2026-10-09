package com.proyecto.servicios.service;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.cuenta.Cuenta;

import java.util.List;

public interface CuentaService {
    Cuenta crearCuentaParaCliente(Cliente cliente);
    Cuenta obtenerPorNumeroCuenta(String numeroCuenta);
    List<Cuenta> obtenerCuentasActivas();
}