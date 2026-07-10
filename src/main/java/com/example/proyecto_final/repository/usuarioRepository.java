package com.example.proyecto_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto_final.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface usuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
    List<Usuario> findByEstadoTrue();
}
