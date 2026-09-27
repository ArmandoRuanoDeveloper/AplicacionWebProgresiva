package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.producto.ProductoSnapshotDocument;
import com.proyecto.servicios.entity.producto.ProductoSnapshotItem;
import com.proyecto.servicios.exception.ProductoServiceException;
import com.proyecto.servicios.model.producto.Producto;
import com.proyecto.servicios.model.producto.ProductoListResponse;
import com.proyecto.servicios.repositorys.producto.ProductoSnapshotRepository;
import com.proyecto.servicios.service.ProductoPersistenceService;
import com.proyecto.servicios.service.ProductoService;
import com.proyecto.servicios.service.ProductoSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ProductoSyncServiceImpl implements ProductoSyncService {

    private final ProductoService productoService;
    private final ProductoSnapshotRepository snapshotRepository;
    private final ProductoPersistenceService productoPersistenceService;

    public ProductoSyncServiceImpl(ProductoService productoService,
                                    ProductoSnapshotRepository snapshotRepository,
                                    ProductoPersistenceService productoPersistenceService) {
        this.productoService = productoService;
        this.snapshotRepository = snapshotRepository;
        this.productoPersistenceService = productoPersistenceService;
    }

    @Override
    @Scheduled(cron = "${product-service.sync-cron:0 0 3 * * *}")
    public void sincronizarCatalogoProductos() {
        log.info("Iniciando sincronización diaria del catálogo de productos");
        try {
            ProductoListResponse response = productoService.consultarServicioExterno();

            ProductoSnapshotDocument snapshot = mapearSnapshot(response);
            snapshotRepository.save(snapshot);
            log.info("Snapshot del catálogo de productos guardado en MongoDB");

            productoPersistenceService.guardarProductosEnPostgres(response.getProductos());

        } catch (ProductoServiceException e) {
            log.error("No fue posible sincronizar el catálogo de productos: {}", e.getMessage());
        }
    }

    private ProductoSnapshotDocument mapearSnapshot(ProductoListResponse response) {
        ProductoSnapshotDocument doc = new ProductoSnapshotDocument();
        doc.setCodigoRespuesta(response.getMensaje() != null ? response.getMensaje().getCodigo() : null);
        doc.setTextoRespuesta(response.getMensaje() != null ? response.getMensaje().getTexto() : null);
        doc.setFechaConsulta(LocalDateTime.now());
        doc.setProductos(response.getProductos().stream().map(this::mapearItem).toList());
        return doc;
    }

    private ProductoSnapshotItem mapearItem(Producto dto) {
        ProductoSnapshotItem item = new ProductoSnapshotItem();
        item.setServicio(dto.getServicio());
        item.setProducto(dto.getProducto());
        item.setIdServicio(dto.getIdServicio());
        item.setIdProducto(dto.getIdProducto());
        item.setIdCatTipoServicio(dto.getIdCatTipoServicio());
        item.setTipoFront(dto.getTipoFront());
        item.setPrecio(dto.getPrecio());
        item.setTipoReferencia(dto.getTipoReferencia());
        item.setLegend(dto.getLegend());
        return item;
    }
}