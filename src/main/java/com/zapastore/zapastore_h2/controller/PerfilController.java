package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.model.usuarios.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@Controller
public class PerfilController {

    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    public PerfilController(UsuarioService usuarioService, PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/cliente/perfil")
    public String verPerfil(HttpSession session, Model model) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) {
            return "redirect:/login";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<PedidoView> pedidosCompletados = pedidoService.findByClienteAndEstado(cliente, "Completado").stream()
                .map(p -> new PedidoView(
                        p.getId(),
                        p.getTotalPagar(),
                        p.getEstado(),
                        p.getFecha().format(formatter)
                ))
                .collect(Collectors.toList());

        model.addAttribute("pedidos", pedidosCompletados);
        return "cliente/perfil";
    }

    public static class PedidoView {
        private Integer id;
        private BigDecimal totalPagar;
        private String estado;
        private String fecha;

        public PedidoView(Integer id, BigDecimal totalPagar, String estado, String fecha) {
            this.id = id;
            this.totalPagar = totalPagar;
            this.estado = estado;
            this.fecha = fecha;
        }

        public Integer getId() {
            return id;
        }

        public BigDecimal getTotalPagar() {
            return totalPagar;
        }

        public String getEstado() {
            return estado;
        }

        public String getFecha() {
            return fecha;
        }
    }
}