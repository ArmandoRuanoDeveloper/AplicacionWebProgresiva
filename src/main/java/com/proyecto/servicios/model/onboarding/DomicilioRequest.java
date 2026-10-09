package com.proyecto.servicios.model.onboarding;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomicilioRequest {

    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 2, max = 100, message = "La calle debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = Reglas.TEXTO, message = "La calle contiene caracteres no permitidos")
    private String calle;

    @NotBlank(message = "El número exterior es obligatorio")
    @Size(max = 10, message = "El número exterior admite máximo 10 caracteres")
    @Pattern(regexp = Reglas.NUMERO, message = "El número exterior no es válido (ej. 10, 10-A, S/N)")
    private String numeroExterior;

    @Size(max = 10, message = "El número interior admite máximo 10 caracteres")
    @Pattern(regexp = Reglas.NUMERO, message = "El número interior no es válido (ej. 4, 4-B)")
    private String numeroInterior;

    @NotBlank(message = "La colonia es obligatoria")
    @Size(min = 2, max = 100, message = "La colonia debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = Reglas.TEXTO, message = "La colonia contiene caracteres no permitidos")
    private String colonia;

    @NotBlank(message = "El municipio es obligatorio")
    @Size(min = 2, max = 100, message = "El municipio debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El municipio solo puede contener letras")
    private String municipio;

    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El estado solo puede contener letras")
    private String estado;

    @NotBlank(message = "El país es obligatorio")
    @Size(min = 2, max = 50, message = "El país debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El país solo puede contener letras")
    private String pais;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = Reglas.CP, message = "El código postal debe tener exactamente 5 dígitos")
    private String codigoPostal;
}