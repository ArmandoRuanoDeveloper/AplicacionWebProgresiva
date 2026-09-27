package com.proyecto.servicios.repositorys.producto;

import com.proyecto.servicios.entity.producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    Optional<ProductoEntity> findByIdServicioAndIdProducto(Integer idServicio, Integer idProducto);
}