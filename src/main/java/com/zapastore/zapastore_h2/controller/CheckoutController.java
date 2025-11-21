package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CheckoutController {

    private final PedidoService pedidoService;

    public CheckoutController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // -------------------------------
    // POST: Finalizar Compra
    // -------------------------------
    @PostMapping("/cliente/checkout")
    public String checkoutPost(HttpSession session, RedirectAttributes redirectAttributes) {

        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión para realizar la compra.");
            return "redirect:/login";
        }

        List<Pedido> pedidosPendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pedidosPendientes.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "No hay pedidos pendientes para pagar.");
            return "redirect:/cliente/home";
        }

        Pedido pedido = pedidosPendientes.get(0);
        pedido.setEstado("Completado");
        pedido.setFecha(LocalDateTime.now());
        pedidoService.save(pedido);

        // No eliminamos el carrito de la sesión, solo dejamos que el estado "Completado" lo excluya naturalmente
        redirectAttributes.addFlashAttribute("pedidoExitoso", pedido);
        return "redirect:/cliente/checkout/success";
    }

    // -------------------------------
    // GET: Página de éxito
    // -------------------------------
    @GetMapping("/cliente/checkout/success")
    public String checkoutSuccess(Model model) {

        // Si no hay pedido en FlashAttribute, redirige al home
        if (!model.containsAttribute("pedidoExitoso")) {
            return "redirect:/cliente/home";
        }

        return "cliente/compraExitosa"; // JSP de éxito
    }
}
