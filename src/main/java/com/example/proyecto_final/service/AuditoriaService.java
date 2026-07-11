package com.example.proyecto_final.service;
import com.example.proyecto_final.entity.Auditoria;
import com.example.proyecto_final.entity.Usuario;
import com.example.proyecto_final.repository.AuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {
    private final AuditoriaRepository auditoriaRepository;
    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

 public void registrar(Usuario usuario, String modulo, String tabla, String operacion,
                          Integer codigoRegistro, String valorAnterior, String valorNuevo,
                          HttpServletRequest request) {
    Auditoria a = new Auditoria();
    a.setUsuario(usuario);
    a.setModulo(modulo);
    a.setTablaAfectada(tabla);
    a.setOperacion(operacion);
    a.setCodigoRegistro(codigoRegistro);
    a.setValorAnterior(valorAnterior);
    a.setValorNuevo(valorNuevo);
if(request != null) {
a.setIpOrigen(request.getRemoteAddr());
a.setEquipo(request.getRemoteHost());
a.setNavegador(request.getHeader("User-Agent"));
}
    
    auditoriaRepository.save(a);
}



}
