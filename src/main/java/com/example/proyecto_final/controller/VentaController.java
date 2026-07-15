package com.example.proyecto_final.controller;

import com.example.proyecto_final.entity.Usuario;
import com.example.proyecto_final.entity.VentaCabecera;
import com.example.proyecto_final.entity.VentaDetalle;
import com.example.proyecto_final.service.ClienteService;
import com.example.proyecto_final.service.PermisoService;
import com.example.proyecto_final.service.ProductoService;
import com.example.proyecto_final.service.UsuarioService;
import com.example.proyecto_final.service.VentaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final PermisoService permisoService;

    public VentaController(VentaService ventaService, ClienteService clienteService,
                           ProductoService productoService, UsuarioService usuarioService,
                           PermisoService permisoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.permisoService = permisoService;
    }

    @GetMapping
    public String listar(Authentication auth, Model model) {
        if (!permisoService.tieneVer(auth, "Ventas")) return "redirect:/inicio?error=sinPermiso";
        model.addAttribute("ventas", ventaService.listarTodas());
        return "ventas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Authentication auth, Model model) {
        if (!permisoService.tieneCrear(auth, "Ventas")) return "redirect:/ventas?error=sinPermiso";
        model.addAttribute("venta", new VentaCabecera());
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        return "ventas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(VentaCabecera cabecera,
                          @RequestParam(required = false) List<Integer> productoId,
                          @RequestParam(required = false) List<Integer> cantidad,
                          @RequestParam(required = false) List<String> tipoVenta,
                          Authentication auth, HttpServletRequest request, HttpSession session) {
        Usuario actual = usuarioService.buscarPorUsuario(auth.getName()).orElseThrow();

        if (cabecera.getCliente() == null || cabecera.getCliente().getCodCliente() == null) {
            return "redirect:/ventas/nuevo?error=sinCliente";
        }

        if (productoId == null || productoId.isEmpty()) {
            return "redirect:/ventas/nuevo?error=sinProductos";
        }

        List<VentaDetalle> detalles = new ArrayList<>();
        for (int i = 0; i < productoId.size(); i++) {
            if (productoId.get(i) == null) continue;

            var prodOpt = productoService.buscarPorId(productoId.get(i));
            if (prodOpt.isEmpty()) {
                return "redirect:/ventas/nuevo?error=productoNoEncontrado";
            }

            int cant = cantidad != null && i < cantidad.size() ? cantidad.get(i) : 1;
            if (cant <= 0) {
                return "redirect:/ventas/nuevo?error=cantidadInvalida";
            }

            VentaDetalle detalle = new VentaDetalle();
            detalle.setProducto(prodOpt.get());
            detalle.setCantidad(cant);
            detalle.setTipoVenta(tipoVenta != null && i < tipoVenta.size() ? tipoVenta.get(i) : "UNIDAD");

            var prod = prodOpt.get();
            if ("UNIDAD".equals(detalle.getTipoVenta()) && prod.getCantidadUnidad() < cant) {
                return "redirect:/ventas/nuevo?error=stockInsuficiente&producto=" + prod.getNombreProducto();
            }
            if ("FRACCION".equals(detalle.getTipoVenta())) {
                int items = prod.getCantidadItem() != null ? prod.getCantidadItem() : 1;
                int fraccionesDisponibles = prod.getCantidadFraccion() + prod.getCantidadUnidad() * items;
                if (fraccionesDisponibles < cant) {
                    return "redirect:/ventas/nuevo?error=stockInsuficiente&producto=" + prod.getNombreProducto();
                }
            }

            detalles.add(detalle);
        }

        if (detalles.isEmpty()) {
            return "redirect:/ventas/nuevo?error=sinProductos";
        }

        try {
            VentaCabecera guardada = ventaService.registrarVenta(cabecera, detalles, actual, request);
            return "redirect:/ventas/nuevo?exito&id=" + guardada.getCodVenta();
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException | jakarta.persistence.OptimisticLockException e) {
            return "redirect:/ventas/nuevo?error=" + java.net.URLEncoder.encode("La informacion fue modificada por otro usuario. Por favor, actualice la pantalla antes de continuar.", java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "redirect:/ventas/nuevo?error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/anular/{id}")
    public String anular(@PathVariable Integer id, Authentication auth, HttpServletRequest request, HttpSession session) {
        if (!permisoService.tieneEliminar(auth, "Anular Venta")) return "redirect:/ventas?error=sinPermiso";
        String redirect2fa = validarYRedireccionar2fa(auth, session, "/ventas");
        if (redirect2fa != null) {
            return redirect2fa;
        }
        Usuario actual = usuarioService.buscarPorUsuario(auth.getName()).orElseThrow();
        try {
            ventaService.anularVenta(id, actual, request);
        } catch (Exception e) {
            return "redirect:/ventas?error=" + e.getMessage();
        }
        return "redirect:/ventas?exitoAnulado";
    }

    private String validarYRedireccionar2fa(Authentication auth, HttpSession session, String targetUrl) {
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";
        Usuario actual = usuarioService.buscarPorUsuario(auth.getName()).orElse(null);
        if (actual == null) return "redirect:/login";
        boolean tiene2fa = actual.getSecretKey2fa() != null && !actual.getSecretKey2fa().isEmpty();
        if (!tiene2fa) {
            return "redirect:/usuarios/2fa/configurar?redirect=" + targetUrl;
        }
        Boolean verificado = (Boolean) session.getAttribute("2fa_verified");
        if (verificado == null || !verificado) {
            return "redirect:/usuarios/2fa/verificar-sesion?redirect=" + targetUrl;
        }
        return null;
    }

    @GetMapping("/detalle/{id}")
    @ResponseBody
    public List<java.util.Map<String, Object>> verDetalle(@PathVariable Integer id) {
        List<VentaDetalle> detalles = ventaService.buscarDetallesPorVenta(id);
        List<java.util.Map<String, Object>> res = new ArrayList<>();
        for (VentaDetalle d : detalles) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("codVentaDetalle", d.getCodDetalle());
            map.put("productoNombre", d.getProducto().getNombreProducto());
            map.put("cantidad", d.getCantidad());
            map.put("precioUnitario", d.getPrecioUnitario());
            map.put("subtotal", d.getSubtotal());
            map.put("tipoVenta", d.getTipoVenta());
            res.add(map);
        }
        return res;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public java.util.Map<String, Object> buscarVenta(@PathVariable Integer id) {
        VentaCabecera v = ventaService.buscarPorId(id).orElseThrow();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("codVenta", v.getCodVenta());
        map.put("comprobante", v.getTipoComprobante() + " " + v.getSerie() + "-" + v.getNumeroCorrelativo());
        map.put("cliente", v.getCliente().getNombreCliente() != null && !v.getCliente().getNombreCliente().isEmpty() ? v.getCliente().getNombreCliente() : (v.getCliente().getRazonSocial() != null ? v.getCliente().getRazonSocial() : ""));
        map.put("subtotal", v.getSubtotal());
        map.put("igv", v.getIgv());
        map.put("total", v.getTotal());
        map.put("fecha", v.getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        map.put("usuario", v.getUsuarioRegistro().getUsuario());
        map.put("estado", v.getEstado() ? "Activo" : "Anulado");
        return map;
    }
}
