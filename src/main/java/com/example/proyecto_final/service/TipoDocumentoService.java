package com.example.proyecto_final.service;

import com.example.proyecto_final.entity.TipoDocumento;
import com.example.proyecto_final.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<TipoDocumento> listarActivos() {
        return tipoDocumentoRepository.findByEstadoTrue();
    }

    public List<TipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }
}
