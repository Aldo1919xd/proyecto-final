package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngresoProductoRepository extends JpaRepository<IngresoProducto, Integer> {
    List<IngresoProducto> findByEstadoTrueOrderByFechaHoraDesc();
}
