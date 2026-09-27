package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.producto.ProductoSnapshotDocument;
import com.proyecto.servicios.exception.ProductoServiceException;
import com.proyecto.servicios.model.producto.Mensaje;
import com.proyecto.servicios.model.producto.ProductoListResponse;
import com.proyecto.servicios.model.producto.Producto;
import com.proyecto.servicios.repositorys.producto.ProductoSnapshotRepository;
import com.proyecto.servicios.service.ProductoPersistenceService;
import com.proyecto.servicios.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoSyncServiceImplTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoSnapshotRepository snapshotRepository;

    @Mock
    private ProductoPersistenceService productoPersistenceService;

    @InjectMocks
    private ProductoSyncServiceImpl productoSyncService;

    @Test
    void sincronizarCatalogoProductos_exitoso_guardaEnMongoYDisparaPostgres() {
        Producto producto = new Producto();
        producto.setIdServicio(71);
        producto.setIdProducto(200);
        producto.setProducto("Amazon $100");

        Mensaje mensaje = new Mensaje();
        mensaje.setCodigo("01");
        mensaje.setTexto("Operacion realizada con exito");

        ProductoListResponse response = new ProductoListResponse();
        response.setMensaje(mensaje);
        response.setProductos(List.of(producto));

        when(productoService.consultarServicioExterno()).thenReturn(response);

        productoSyncService.sincronizarCatalogoProductos();

        ArgumentCaptor<ProductoSnapshotDocument> snapshotCaptor = ArgumentCaptor.forClass(ProductoSnapshotDocument.class);
        verify(snapshotRepository).save(snapshotCaptor.capture());

        ProductoSnapshotDocument snapshotGuardado = snapshotCaptor.getValue();
        assertEquals("01", snapshotGuardado.getCodigoRespuesta());
        assertEquals(1, snapshotGuardado.getProductos().size());
        assertEquals("Amazon $100", snapshotGuardado.getProductos().get(0).getProducto());

        verify(productoPersistenceService).guardarProductosEnPostgres(response.getProductos());
    }

    @Test
    void sincronizarCatalogoProductos_errorDelServicioExterno_noGuardaNiEnMongoNiEnPostgres() {
        when(productoService.consultarServicioExterno())
                .thenThrow(new ProductoServiceException("El servicio externo de productos respondió con un error"));

        productoSyncService.sincronizarCatalogoProductos();

        verify(snapshotRepository, never()).save(any());
        verify(productoPersistenceService, never()).guardarProductosEnPostgres(any());
    }
}