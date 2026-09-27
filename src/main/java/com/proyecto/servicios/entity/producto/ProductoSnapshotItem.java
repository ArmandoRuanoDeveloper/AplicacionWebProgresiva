package com.proyecto.servicios.entity.producto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoSnapshotItem {
    private String servicio;
    private String producto;
    private Integer idServicio;
    private Integer idProducto;
    private Integer idCatTipoServicio;
    private Integer tipoFront;
    private Double precio;
    private String tipoReferencia;
    private String legend;
}