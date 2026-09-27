package com.proyecto.servicios.service;

import com.proyecto.servicios.model.producto.Producto;

import java.util.List;

public interface ProductoPersistenceService {
    void guardarProductosEnPostgres(List<Producto> productos);
}