package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    List<Rol> findByEstadoTrue();
}
