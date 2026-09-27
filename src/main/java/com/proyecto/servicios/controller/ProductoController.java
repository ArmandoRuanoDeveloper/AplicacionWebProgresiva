package com.proyecto.servicios.controller;

import com.proyecto.servicios.entity.producto.ProductoEntity;
import com.proyecto.servicios.repositorys.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping(value = "/productos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductoEntity>> obtenerProductos() {
        List<ProductoEntity> productos = productoRepository.findAll();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
}