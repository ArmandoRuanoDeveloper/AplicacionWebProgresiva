package com.proyecto.servicios.model.onboarding;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaResponse {
    private String numeroCuenta;
    private BigDecimal saldo;
    private String estatus;
}