package com.example.proyecto_final.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.proyecto_final.entity.RolFuncionalidad;
import com.example.proyecto_final.entity.Usuario;
import com.example.proyecto_final.repository.RolFuncionalidadRepository;

@Service
public class PermisoService {
    
    private final UsuarioService usuarioService;
    private final RolFuncionalidadRepository rolFuncionalidadRepository;

    public PermisoService(UsuarioService usuarioService, RolFuncionalidadRepository rolFuncionalidadRepository) {
        this.usuarioService = usuarioService;
        this.rolFuncionalidadRepository = rolFuncionalidadRepository;
    }

    private boolean check(Authentication auth, String funcionalidadNombre, Function<RolFuncionalidad, Boolean> permisoFn){
        if(auth == null || !auth.isAuthenticated()) return false;
        Usuario usuario = usuarioService.buscarPorUsuario(auth.getName()).orElse(null);
        if(usuario == null) return false;
        List<RolFuncionalidad> rfList = rolFuncionalidadRepository.findByRolIdRol(usuario.getRol().getIdRol());
        return rfList.stream()
                .filter(rf -> rf.getFuncionalidad().getNombre().equals(funcionalidadNombre))
                .findFirst()
                .map(permisoFn)
                .orElse(false);
    }

    public boolean tieneVer(Authentication auth, String funcionalidad){
        return check(auth, funcionalidad, RolFuncionalidad::getVer);
    }

    public boolean tieneCrear(Authentication auth, String funcionalidad){
        return check(auth, funcionalidad, RolFuncionalidad::getCrear);
    }

    public boolean tieneEditar(Authentication auth, String funcionalidad){
        return check(auth, funcionalidad, RolFuncionalidad::getEditar);
    }

    public boolean tieneEliminar(Authentication auth, String funcionalidad){
        return check(auth, funcionalidad, RolFuncionalidad::getEliminar);
    }
}
