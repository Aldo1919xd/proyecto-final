package com.example.proyecto_final.service;
import com.example.proyecto_final.entity.Funcionalidad;
import com.example.proyecto_final.entity.Rol;
import com.example.proyecto_final.entity.RolFuncionalidad;
import com.example.proyecto_final.entity.Usuario;
import com.example.proyecto_final.repository.FuncionalidadRepository;
import com.example.proyecto_final.repository.RolFuncionalidadRepository;
import com.example.proyecto_final.repository.RolRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RolService {
    private final RolRepository rolRepository;
    private final FuncionalidadRepository funcionalidadRepository;
    private final RolFuncionalidadRepository rolFuncionalidadRepository;
    private final AuditoriaService auditoriaService;

    public RolService(RolRepository rolRepository, FuncionalidadRepository funcionalidadRepository,
                      RolFuncionalidadRepository rolFuncionalidadRepository,
                      AuditoriaService auditoriaService) {
        this.rolRepository = rolRepository;
        this.funcionalidadRepository = funcionalidadRepository;
        this.rolFuncionalidadRepository = rolFuncionalidadRepository;
        this.auditoriaService = auditoriaService;
    }


    
}
