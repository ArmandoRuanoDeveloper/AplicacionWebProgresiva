package com.proyecto.servicios.model.onboarding;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ClienteResponse {
    private Integer id;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String curp;
    private String rfc;
    private String correo;
    private String telefonoMovil;
    private Boolean activo;
    private List<CuentaResponse> cuentas;
}