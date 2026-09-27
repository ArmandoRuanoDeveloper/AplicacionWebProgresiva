package com.proyecto.servicios.entity.producto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "productos_snapshot")
public class ProductoSnapshotDocument {

    @Id
    private String id;

    private String codigoRespuesta;
    private String textoRespuesta;
    private List<ProductoSnapshotItem> productos;
    private LocalDateTime fechaConsulta;
}