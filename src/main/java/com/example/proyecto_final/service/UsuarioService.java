package com.example.proyecto_final.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.proyecto_final.entity.Usuario;
import com.example.proyecto_final.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    public List<Usuario> listarActivos(){
        return usuarioRepository.findByEstadoTrue();
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorUsuario(String usuario){
        return usuarioRepository.findByUsuario(usuario);
    }

    @Transactional
    public Usuario guardar(Usuario usuario,Usuario usuarioActual, HttpServletRequest request) {
        boolean esNuevo = usuario.getIdUsuario() == null;
        if(esNuevo){
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setUsuarioCreacion(usuario);
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow();
            if(usuario.getPassword() != null && !usuario.getPassword().isEmpty()
                    && !usuario.getPassword().equals(existente.getPassword())){
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                usuario.setPassword(existente.getPassword());
            }
            usuario.setFechaModificacion(LocalDateTime.now());
        }
        Usuario guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar(usuarioActual, "Seguridad", "Usuario", 
                esNuevo ? "INSERT" : "UPDATE", 
                guardado.getIdUsuario(),
                esNuevo ? null : "{\"usuario\":\"" + usuario.getUsuario() + "\"}", 
            "{\"usuario\":\"" + guardado.getUsuario() + "\"}",
                request);
        return guardado;
    }

    @Transactional
    public void cambiarPassword(Integer idUsuario, String nuevoPassword, Usuario usuarioActual,HttpServletRequest request){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        usuario.setPassword(passwordEncoder.encode(nuevoPassword));
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        auditoriaService.registrar(usuarioActual, "Seguridad", "Usuario", 
                "UPDATE", idUsuario, null, "{\"password\":\"Cambiada\"}", request);
    }

    @Transactional
    public void actualizar2fa(Usuario usuario){
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarLogico(Integer id,Usuario usuarioActual, HttpServletRequest request){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        if(usuario.getIdUsuario() == 1){
            throw new RuntimeException("No se puede eliminar al superUsuario");
        }
        usuario.setEstado(false);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        auditoriaService.registrar(usuarioActual, "Seguridad", "Usuario", 
                "DELETE", id, "{\"estado\":true}", "{\"estado\":false}", request);
    }
}
