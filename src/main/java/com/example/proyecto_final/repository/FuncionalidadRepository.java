package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Funcionalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FuncionalidadRepository extends JpaRepository<Funcionalidad, Integer> {
    List<Funcionalidad> findByPadreIsNullOrderByNombreAsc();
    java.util.Optional<Funcionalidad> findByNombre(String nombre);
}