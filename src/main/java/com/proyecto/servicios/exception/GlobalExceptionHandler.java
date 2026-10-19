package com.proyecto.servicios.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.proyecto.servicios.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return respuesta(HttpStatus.BAD_REQUEST, "Error de validación", errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        Throwable causa = ex.getCause();
        Map<String, String> errores = new LinkedHashMap<>();
        String mensaje = "El cuerpo de la petición es obligatorio o no es un JSON válido";

        if (causa instanceof UnrecognizedPropertyException upe) {
            mensaje = "La petición contiene campos no permitidos";
            errores.put(upe.getPropertyName(), "Este campo no existe o no puede ser enviado por el cliente");
        } else if (causa instanceof InvalidFormatException ife && !ife.getPath().isEmpty()) {
            mensaje = "La petición contiene valores con formato incorrecto";
            errores.put(ife.getPath().get(ife.getPath().size() - 1).getFieldName(),
                    "El valor tiene un formato inválido (fechas: yyyy-MM-dd)");
        }
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, errores.isEmpty() ? null : errores);
    }

    @ExceptionHandler({ClienteNoEncontradoException.class, CuentaNoEncontradaException.class,
            UsuarioNoEncontradoException.class})
    public ResponseEntity<ApiResponse<Void>> manejarNoEncontrado(RuntimeException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({CurpDuplicadaException.class, RfcDuplicadoException.class,
            CorreoDuplicadoException.class, ClienteYaRegistradoException.class})
    public ResponseEntity<ApiResponse<Void>> manejarDuplicado(RuntimeException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad en base de datos: {}", ex.getMostSpecificCause().getMessage());
        return respuesta(HttpStatus.CONFLICT,
                "El dato viola una restricción de la base de datos (valor duplicado o longitud excedida)", null);
    }

    @ExceptionHandler({CredencialesInvalidasException.class, UsuarioInactivoException.class})
    public ResponseEntity<ApiResponse<Void>> manejarAutenticacion(RuntimeException ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> manejarMetodo(HttpRequestMethodNotSupportedException ex) {
        return respuesta(HttpStatus.METHOD_NOT_ALLOWED, "El método " + ex.getMethod() + " no está permitido en esta ruta", null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> manejarMediaType(HttpMediaTypeNotSupportedException ex) {
        return respuesta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El tipo de contenido no es compatible; usa application/json", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> manejarRutaInexistente(NoResourceFoundException ex) {
        return respuesta(HttpStatus.NOT_FOUND, "El recurso solicitado no existe", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> manejarInesperado(Exception ex) {
        log.error("Error no controlado", ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor; el incidente quedó registrado para su revisión", null);
    }

    private ResponseEntity<ApiResponse<Void>> respuesta(HttpStatus status, String mensaje, Map<String, String> errores) {
        return ResponseEntity.status(status).body(ApiResponse.error(status, mensaje, errores));
    }
}