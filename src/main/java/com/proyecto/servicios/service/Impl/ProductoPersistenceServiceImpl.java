package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.producto.ProductoEntity;
import com.proyecto.servicios.model.producto.Producto;
import com.proyecto.servicios.repositorys.producto.ProductoRepository;
import com.proyecto.servicios.service.ProductoPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductoPersistenceServiceImpl implements ProductoPersistenceService {

    private final ProductoRepository productoRepository;

    public ProductoPersistenceServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Async
    public void guardarProductosEnPostgres(List<Producto> productos) {
        log.info("Iniciando guardado en segundo plano de {} productos en PostgreSQL", productos.size());

        for (Producto dto : productos) {
            try {
                ProductoEntity producto = productoRepository
                        .findByIdServicioAndIdProducto(dto.getIdServicio(), dto.getIdProducto())
                        .orElseGet(ProductoEntity::new);

                producto.setIdServicio(dto.getIdServicio());
                producto.setIdProducto(dto.getIdProducto());
                producto.setServicio(dto.getServicio());
                producto.setProducto(dto.getProducto());
                producto.setIdCatTipoServicio(dto.getIdCatTipoServicio());
                producto.setTipoFront(dto.getTipoFront());
                producto.setPrecio(dto.getPrecio());
                producto.setTipoReferencia(dto.getTipoReferencia());
                producto.setLegend(dto.getLegend());

                productoRepository.save(producto);
            } catch (Exception e) {
                log.error("Error al guardar el producto idServicio={} idProducto={} en PostgreSQL",
                        dto.getIdServicio(), dto.getIdProducto());
            }
        }

        log.info("Guardado en segundo plano de productos en PostgreSQL finalizado");
    }
}