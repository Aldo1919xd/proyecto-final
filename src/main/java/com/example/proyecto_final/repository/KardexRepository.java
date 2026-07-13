package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KardexRepository extends JpaRepository<Kardex, Integer> {
    List<Kardex> findByProductoCodProductoOrderByFechaHoraAsc(Integer codProducto);

    List<Kardex> findByProductoCodProductoOrderByFechaHoraDesc(Integer codProducto);
}
