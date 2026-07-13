package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Integer> {
    List<VentaDetalle> findByVentaCodVenta(Integer codVenta);
}
