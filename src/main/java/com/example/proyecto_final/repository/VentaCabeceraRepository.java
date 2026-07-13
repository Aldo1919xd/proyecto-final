package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.VentaCabecera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaCabeceraRepository extends JpaRepository<VentaCabecera, Integer> {
    List<VentaCabecera> findByEstadoTrueOrderByFechaHoraDesc();
}
