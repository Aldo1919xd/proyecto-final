package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.ProductoComposicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoComposicionRepository extends JpaRepository<ProductoComposicion, Integer> {
    List<ProductoComposicion> findByProductoPack_CodProducto(Integer codProductoPack);
    void deleteByProductoPack_CodProducto(Integer codProductoPack);
}
