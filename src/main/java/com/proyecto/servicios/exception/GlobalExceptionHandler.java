package com.proyecto.servicios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Error de validación", errores);
    }

    @ExceptionHandler({ClienteNoEncontradoException.class, CuentaNoEncontradaException.class, UsuarioNoEncontradoException.class})
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RuntimeException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({CurpDuplicadaException.class, RfcDuplicadoException.class, CorreoDuplicadoException.class, ClienteYaRegistradoException.class})
    public ResponseEntity<Map<String, Object>> manejarDuplicado(RuntimeException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler({CredencialesInvalidasException.class, UsuarioInactivoException.class})
    public ResponseEntity<Map<String, Object>> manejarAutenticacion(RuntimeException ex) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje, Map<String, String> detalles) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("mensaje", mensaje);
        if (detalles != null) body.put("errores", detalles);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "status", 400,
            "error", "El cuerpo de la petición no es un JSON válido o tiene un valor con formato incorrecto (ej. fecha yyyy-MM-dd)"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(Map.of(
            "status", 409,
            "error", "El dato viola una restricción de la base de datos (duplicado o longitud excedida)"));
    }
}