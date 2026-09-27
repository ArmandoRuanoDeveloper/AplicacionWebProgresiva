package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.client.ProductoServiceClient;
import com.proyecto.servicios.entity.gestopago.GestoPagoToken;
import com.proyecto.servicios.exception.ProductoServiceException;
import com.proyecto.servicios.model.producto.ProductoListResponse;
import com.proyecto.servicios.model.producto.Producto;
import com.proyecto.servicios.service.GestoPagoTokenService;
import com.proyecto.servicios.service.ProductoService;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoServiceClient productServiceClient;
    private final GestoPagoTokenService gestoPagoTokenService;

    @Value("${gestopago.auth.id-distribuidor}")
    private Integer idDistribuidor;

    @Value("${gestopago.auth.codigo-dispositivo}")
    private String codigoDispositivo;

    @Value("${product-service.api-key:}")
    private String apiKey;

    public ProductoServiceImpl(ProductoServiceClient productServiceClient,
                                GestoPagoTokenService gestoPagoTokenService) {
        this.productServiceClient = productServiceClient;
        this.gestoPagoTokenService = gestoPagoTokenService;
    }

    @Override
    public ProductoListResponse consultarServicioExterno() {
        log.info("Iniciando invocación al servicio externo de productos");

        GestoPagoToken tokenActivo = gestoPagoTokenService
                .obtenerTokenActivo(idDistribuidor, codigoDispositivo)
                .orElseThrow(() -> new ProductoServiceException("No hay un token vigente de GestoPago disponible"));

        String apiKeyHeader = (apiKey == null || apiKey.isBlank()) ? null : apiKey;

        try {
            ProductoListResponse response = productServiceClient.getProductList("Bearer " + tokenActivo.getToken(), apiKeyHeader);
            log.info("Invocación al servicio externo de productos finalizada correctamente");
            return response;

        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
            log.error("Error de autenticación al consumir el servicio externo de productos (status={})", e.status());
            throw new ProductoServiceException("No fue posible autenticar con el servicio externo de productos", e);

        } catch (RetryableException e) {
            log.error("Timeout al consumir el servicio externo de productos");
            throw new ProductoServiceException("El servicio externo de productos no respondió a tiempo", e);

        } catch (FeignException e) {
            log.error("Respuesta no exitosa del servicio externo de productos (status={})", e.status());
            throw new ProductoServiceException("El servicio externo de productos respondió con un error", e);

        } catch (Exception e) {
            log.error("Error de comunicación al consumir el servicio externo de productos");
            throw new ProductoServiceException("Ocurrió un error al comunicarse con el servicio externo de productos", e);
        }
    }

    @Override
    public List<Producto> obtenerListaProductos() {
        return consultarServicioExterno().getProductos();
    }
}