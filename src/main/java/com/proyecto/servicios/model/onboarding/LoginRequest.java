package com.proyecto.servicios.model.onboarding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 72, message = "La contraseña debe tener máximo 72 caracteres")
    private String password;

    public void setCorreo(String correo) { this.correo = correo == null ? null : correo.toLowerCase(Locale.ROOT); }
}