package com.zapastore.zapastore_h2.model.usuarios;

import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        // Formato de fecha hasta minutos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Obtener solo pedidos completados y formatear la fecha como String
        List<PedidoView> pedidosCompletados = pedidoService.findByCliente(cliente).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .map(p -> new PedidoView(
                        p.getId(),
                        p.getTotalPagar(),
                        p.getEstado(),
                        p.getFecha().format(formatter)
                ))
                .collect(Collectors.toList());

        model.addAttribute("pedidos", pedidosCompletados);
        return "cliente/perfil"; // JSP de perfil
    }

    // DTO para la vista
    public static class PedidoView {
        private Integer id;
        private java.math.BigDecimal totalPagar;
        private String estado;
        private String fecha;

        public PedidoView(Integer id, java.math.BigDecimal totalPagar, String estado, String fecha) {
            this.id = id;
            this.totalPagar = totalPagar;
            this.estado = estado;
            this.fecha = fecha;
        }

        // Getters
        public Integer getId() { return id; }
        public java.math.BigDecimal getTotalPagar() { return totalPagar; }
        public String getEstado() { return estado; }
        public String getFecha() { return fecha; }
    }
}
