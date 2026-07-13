package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstadoTrue();
    List<Producto> findByNombreProductoContainingIgnoreCaseAndEstadoTrue(String nombre);
}
