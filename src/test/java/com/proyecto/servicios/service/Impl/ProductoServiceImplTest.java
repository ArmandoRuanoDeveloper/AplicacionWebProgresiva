package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.client.ProductoServiceClient;
import com.proyecto.servicios.entity.gestopago.GestoPagoToken;
import com.proyecto.servicios.exception.ProductoServiceException;
import com.proyecto.servicios.model.producto.ProductoListResponse;
import com.proyecto.servicios.model.producto.Producto;
import com.proyecto.servicios.service.GestoPagoTokenService;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoServiceClient productServiceClient;

    @Mock
    private GestoPagoTokenService gestoPagoTokenService;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private GestoPagoToken tokenActivo;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productoService, "idDistribuidor", 123);
        ReflectionTestUtils.setField(productoService, "codigoDispositivo", "DISP-TEST");
        ReflectionTestUtils.setField(productoService, "apiKey", "");

        tokenActivo = new GestoPagoToken();
        tokenActivo.setToken("token-de-prueba");

        when(gestoPagoTokenService.obtenerTokenActivo(anyInt(), anyString()))
                .thenReturn(Optional.of(tokenActivo));
    }

    @Test
    void obtenerListaProductos_exitoso_devuelveLaLista() {
        Producto producto = new Producto();
        producto.setIdProducto(200);
        producto.setIdServicio(71);
        producto.setProducto("Amazon $100");

        ProductoListResponse response = new ProductoListResponse();
        response.setProductos(List.of(producto));

        when(productServiceClient.getProductList(anyString(), isNull()))
                .thenReturn(response);

        List<Producto> resultado = productoService.obtenerListaProductos();

        assertEquals(1, resultado.size());
        assertEquals("Amazon $100", resultado.get(0).getProducto());
        verify(productServiceClient).getProductList("Bearer token-de-prueba", null);
    }

    @Test
    void obtenerListaProductos_noAutorizado_lanzaProductServiceException() {
        FeignException.Unauthorized errorAuth = mock(FeignException.Unauthorized.class);
        when(errorAuth.status()).thenReturn(401);

        when(productServiceClient.getProductList(anyString(), isNull())).thenThrow(errorAuth);

        ProductoServiceException ex = assertThrows(ProductoServiceException.class,
                () -> productoService.obtenerListaProductos());

        assertTrue(ex.getMessage().contains("autenticar"));
    }

    @Test
    void obtenerListaProductos_timeout_lanzaProductServiceException() {
        RetryableException timeoutException = mock(RetryableException.class);

        when(productServiceClient.getProductList(anyString(), isNull())).thenThrow(timeoutException);

        ProductoServiceException ex = assertThrows(ProductoServiceException.class,
                () -> productoService.obtenerListaProductos());

        assertTrue(ex.getMessage().contains("no respondió a tiempo"));
    }

    @Test
    void obtenerListaProductos_respuestaNoExitosa_lanzaProductServiceException() {
        FeignException errorGenerico = mock(FeignException.class);
        when(errorGenerico.status()).thenReturn(500);

        when(productServiceClient.getProductList(anyString(), isNull())).thenThrow(errorGenerico);

        ProductoServiceException ex = assertThrows(ProductoServiceException.class,
                () -> productoService.obtenerListaProductos());

        assertTrue(ex.getMessage().contains("respondió con un error"));
    }

    @Test
    void obtenerListaProductos_errorDeComunicacion_lanzaProductServiceException() {
        when(productServiceClient.getProductList(anyString(), isNull()))
                .thenThrow(new RuntimeException("Connection reset"));

        ProductoServiceException ex = assertThrows(ProductoServiceException.class,
                () -> productoService.obtenerListaProductos());

        assertTrue(ex.getMessage().contains("comunicarse"));
    }

    @Test
    void obtenerListaProductos_sinTokenVigente_lanzaProductServiceException() {
        when(gestoPagoTokenService.obtenerTokenActivo(anyInt(), anyString()))
                .thenReturn(Optional.empty());

        ProductoServiceException ex = assertThrows(ProductoServiceException.class,
                () -> productoService.obtenerListaProductos());

        assertTrue(ex.getMessage().contains("token vigente"));
    }
}