package com.example.proyecto_final.repository;

import com.example.proyecto_final.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    List<TipoDocumento> findByEstadoTrue();
}
