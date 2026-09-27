package com.proyecto.servicios.service;

import com.proyecto.servicios.model.producto.ProductoListResponse;
import com.proyecto.servicios.model.producto.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> obtenerListaProductos();
    ProductoListResponse consultarServicioExterno();
}