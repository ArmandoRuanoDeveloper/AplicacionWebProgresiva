package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.cuenta.Cuenta;
import com.proyecto.servicios.exception.CuentaNoEncontradaException;
import com.proyecto.servicios.repositorys.cuenta.CuentaRepository;
import com.proyecto.servicios.service.CuentaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${cuenta.saldo-inicial:0.00}")
    private BigDecimal saldoInicial;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta crearCuentaParaCliente(Cliente cliente) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(generarNumeroCuentaUnico());
        cuenta.setSaldo(saldoInicial);
        cuenta.setEstatus("ACTIVA");
        cuenta.setCliente(cliente);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta obtenerPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException("No existe una cuenta con número " + numeroCuenta));
    }

    @Override
    public List<Cuenta> obtenerCuentasActivas() {
        return cuentaRepository.findByEstatus("ACTIVA");
    }

    private String generarNumeroCuentaUnico() {
        String numero;
        do {
            numero = String.format("%016d", (long) (RANDOM.nextDouble() * 1_0000_0000_0000_0000L));
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }
}