package com.proyecto.servicios.repositorys.producto;

import com.proyecto.servicios.entity.producto.ProductoSnapshotDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoSnapshotRepository extends MongoRepository<ProductoSnapshotDocument, String> {
}