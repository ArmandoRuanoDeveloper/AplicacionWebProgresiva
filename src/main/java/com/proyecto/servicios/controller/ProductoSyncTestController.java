package com.proyecto.servicios.controller;

import com.proyecto.servicios.service.ProductoSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductoSyncTestController {

    @Autowired
    private ProductoSyncService productoSyncService;

    @PostMapping("/productos/sincronizar")
    public ResponseEntity<String> sincronizarManualmente() {
        productoSyncService.sincronizarCatalogoProductos();
        return ResponseEntity.ok("Sincronización disparada, revisa la consola/logs");
    }
}