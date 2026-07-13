package com.example.proyecto_final.controller;

import com.example.proyecto_final.entity.Kardex;
import com.example.proyecto_final.repository.KardexRepository;
import com.example.proyecto_final.service.PermisoService;
import com.example.proyecto_final.service.ProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/kardex")
public class KardexController {

    private final KardexRepository kardexRepository;
    private final ProductoService productoService;
    private final PermisoService permisoService;

    public KardexController(KardexRepository kardexRepository, ProductoService productoService,
                            PermisoService permisoService) {
        this.kardexRepository = kardexRepository;
        this.productoService = productoService;
        this.permisoService = permisoService;
    }

    @GetMapping
    public String resumen(@RequestParam(required = false) Integer productoId, Authentication auth, Model model) {
        if (!permisoService.tieneVer(auth, "Kardex")) return "redirect:/inicio?error=sinPermiso";
        model.addAttribute("productos", productoService.listarActivos());
        if (productoId != null) {
            List<Kardex> movimientos = kardexRepository.findByProductoCodProductoOrderByFechaHoraDesc(productoId);
            model.addAttribute("movimientos", movimientos);
            model.addAttribute("productoSeleccionado", productoService.buscarPorId(productoId).orElse(null));
        }
        return "kardex/resumen";
    }
}
