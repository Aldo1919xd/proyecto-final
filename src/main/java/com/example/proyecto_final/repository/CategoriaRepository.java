package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByEstadoTrue();
}
