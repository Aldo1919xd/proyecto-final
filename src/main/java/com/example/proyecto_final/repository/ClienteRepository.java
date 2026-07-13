package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByEstadoTrue();
    List<Cliente> findByNombreClienteContainingIgnoreCaseAndEstadoTrue(String nombre);
}
