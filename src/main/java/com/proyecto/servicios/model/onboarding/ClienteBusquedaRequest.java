package com.proyecto.servicios.model.onboarding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
public class ClienteBusquedaRequest {

    @Positive(message = "El id debe ser un número positivo")
    private Integer id;

    @Size(max = 50, message = "El nombre admite máximo 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El nombre solo puede contener letras, con un solo espacio entre palabras")
    private String nombre;

    @Size(max = 50, message = "El apellido paterno admite máximo 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El apellido paterno solo puede contener letras, con un solo espacio entre palabras")
    private String apellidoPaterno;

    @Size(max = 50, message = "El apellido materno admite máximo 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El apellido materno solo puede contener letras, con un solo espacio entre palabras")
    private String apellidoMaterno;

    @Size(min = 18, max = 18, message = "La CURP debe tener exactamente 18 caracteres")
    @Pattern(regexp = Reglas.CURP, message = "El formato de la CURP no es válido")
    private String curp;

    @Size(min = 12, max = 13, message = "El RFC debe tener 12 o 13 caracteres")
    @Pattern(regexp = Reglas.RFC, message = "El formato del RFC no es válido")
    private String rfc;

    @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
    @Pattern(regexp = Reglas.CORREO, message = "El formato del correo no es válido")
    private String correo;

    private Boolean activo;

    private LocalDate fechaRegistroDesde;
    private LocalDate fechaRegistroHasta;

    @Pattern(regexp = "\\d{16}", message = "El número de cuenta debe tener exactamente 16 dígitos")
    private String numeroCuenta;

    @Pattern(regexp = "ACTIVA|INACTIVA", message = "El estatus de la cuenta debe ser ACTIVA o INACTIVA")
    private String estatusCuenta;

    @DecimalMin(value = "0.00", message = "El saldo mínimo no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El saldo mínimo admite máximo 12 enteros y 2 decimales")
    private BigDecimal saldoMinimo;

    @DecimalMin(value = "0.00", message = "El saldo máximo no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El saldo máximo admite máximo 12 enteros y 2 decimales")
    private BigDecimal saldoMaximo;

    @JsonIgnore
    @AssertTrue(message = "La fecha de registro inicial no puede ser posterior a la final")
    public boolean isRangoFechasValido() {
        return fechaRegistroDesde == null || fechaRegistroHasta == null
                || !fechaRegistroDesde.isAfter(fechaRegistroHasta);
    }

    @JsonIgnore
    @AssertTrue(message = "El saldo mínimo no puede ser mayor al saldo máximo")
    public boolean isRangoSaldoValido() {
        return saldoMinimo == null || saldoMaximo == null || saldoMinimo.compareTo(saldoMaximo) <= 0;
    }

    public boolean tieneFiltrosDeCuenta() {
        return numeroCuenta != null || estatusCuenta != null || saldoMinimo != null || saldoMaximo != null;
    }

    public void setCurp(String curp) { this.curp = curp == null ? null : curp.toUpperCase(Locale.ROOT); }
    public void setRfc(String rfc) { this.rfc = rfc == null ? null : rfc.toUpperCase(Locale.ROOT); }
    public void setCorreo(String correo) { this.correo = correo == null ? null : correo.toLowerCase(Locale.ROOT); }
}