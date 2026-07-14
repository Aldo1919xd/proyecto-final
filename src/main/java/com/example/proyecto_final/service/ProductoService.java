package com.example.proyecto_final.service;

import com.example.proyecto_final.entity.Producto;
import com.example.proyecto_final.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByEstadoTrue();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }
}
