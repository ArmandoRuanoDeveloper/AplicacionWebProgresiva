package com.proyecto.servicios.controller;

import com.proyecto.servicios.entity.cuenta.Cuenta;
import com.proyecto.servicios.model.onboarding.CuentaResponse;
import com.proyecto.servicios.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/cuentas/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> obtenerPorNumeroCuenta(@PathVariable String numeroCuenta) {
        Cuenta cuenta = cuentaService.obtenerPorNumeroCuenta(numeroCuenta);
        CuentaResponse response = new CuentaResponse();
        response.setNumeroCuenta(cuenta.getNumeroCuenta());
        response.setSaldo(cuenta.getSaldo());
        response.setEstatus(cuenta.getEstatus());
        return ResponseEntity.ok(response);
    }
}