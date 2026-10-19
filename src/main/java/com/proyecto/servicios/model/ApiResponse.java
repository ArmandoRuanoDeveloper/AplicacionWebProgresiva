package com.proyecto.servicios.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String mensaje;
    private final T datos;
    private final Map<String, String> errores;

    private ApiResponse(int status, String mensaje, T datos, Map<String, String> errores) {
        this.status = status;
        this.mensaje = mensaje;
        this.datos = datos;
        this.errores = errores;
    }

    public static <T> ApiResponse<T> exito(HttpStatus status, String mensaje, T datos) {
        return new ApiResponse<>(status.value(), mensaje, datos, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String mensaje, Map<String, String> errores) {
        return new ApiResponse<>(status.value(), mensaje, null, errores);
    }
}