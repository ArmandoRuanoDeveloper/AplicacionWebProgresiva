package com.proyecto.servicios.model.onboarding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.servicios.validation.MayorDeEdad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
public class ClienteRegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El nombre solo puede contener letras, con un solo espacio entre palabras")
    private String nombre;

    @Size(min = 2, max = 50, message = "El segundo nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El segundo nombre solo puede contener letras, con un solo espacio entre palabras")
    private String segundoNombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El apellido paterno solo puede contener letras, con un solo espacio entre palabras")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El apellido materno solo puede contener letras, con un solo espacio entre palabras")
    private String apellidoMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    @MayorDeEdad
    private LocalDate fechaNacimiento;

    @JsonIgnore
    @AssertTrue(message = "La fecha de nacimiento no es válida (año mínimo 1900)")
    public boolean isFechaNacimientoPlausible() {
        return fechaNacimiento == null || fechaNacimiento.getYear() >= 1900;
    }

    @NotBlank(message = "La CURP es obligatoria")
    @Size(min = 18, max = 18, message = "La CURP debe tener exactamente 18 caracteres")
    @Pattern(regexp = Reglas.CURP, message = "El formato de la CURP no es válido")
    private String curp;

    @NotBlank(message = "El RFC es obligatorio")
    @Size(min = 12, max = 13, message = "El RFC debe tener 12 o 13 caracteres")
    @Pattern(regexp = Reglas.RFC, message = "El formato del RFC no es válido")
    private String rfc;

    @NotBlank(message = "El sexo es obligatorio")
    @Size(min = 1, max = 20, message = "El sexo debe tener máximo 20 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El sexo solo puede contener letras")
    private String sexo;

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Size(min = 2, max = 50, message = "La nacionalidad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "La nacionalidad solo puede contener letras")
    private String nacionalidad;

    @NotBlank(message = "El estado civil es obligatorio")
    @Size(min = 2, max = 30, message = "El estado civil debe tener entre 2 y 30 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "El estado civil solo puede contener letras")
    private String estadoCivil;

    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
    @Pattern(regexp = Reglas.CORREO, message = "El formato del correo no es válido")
    private String correo;

    @NotBlank(message = "El teléfono móvil es obligatorio")
    @Pattern(regexp = Reglas.TELEFONO, message = "El teléfono móvil debe contener exactamente 10 dígitos")
    private String telefonoMovil;

    @Pattern(regexp = Reglas.TELEFONO, message = "El teléfono alternativo debe contener exactamente 10 dígitos")
    private String telefonoAlternativo;

    @NotNull(message = "El domicilio es obligatorio")
    @Valid
    private DomicilioRequest domicilio;

    @NotBlank(message = "La ocupación es obligatoria")
    @Size(min = 2, max = 80, message = "La ocupación debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = Reglas.NOMBRE, message = "La ocupación solo puede contener letras")
    private String ocupacion;

    @NotBlank(message = "La empresa es obligatoria")
    @Size(min = 2, max = 100, message = "La empresa debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = Reglas.TEXTO, message = "La empresa contiene caracteres no permitidos")
    private String empresa;

    @NotNull(message = "El ingreso mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El ingreso mensual debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El ingreso mensual admite máximo 10 enteros y 2 decimales")
    private BigDecimal ingresoMensual;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    @Pattern(regexp = Reglas.PASSWORD,
             message = "La contraseña debe incluir mayúscula, minúscula, número y carácter especial, sin espacios ni emojis")
    private String password;

    // Normalización: se ejecuta antes de validar
    public void setCurp(String curp) { this.curp = curp == null ? null : curp.toUpperCase(Locale.ROOT); }
    public void setRfc(String rfc) { this.rfc = rfc == null ? null : rfc.toUpperCase(Locale.ROOT); }
    public void setCorreo(String correo) { this.correo = correo == null ? null : correo.toLowerCase(Locale.ROOT); }
}