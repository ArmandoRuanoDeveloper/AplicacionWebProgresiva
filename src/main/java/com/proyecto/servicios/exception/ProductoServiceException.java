package com.proyecto.servicios.exception;

public class ProductoServiceException extends RuntimeException {

    public ProductoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductoServiceException(String message) {
        super(message);
    }
}