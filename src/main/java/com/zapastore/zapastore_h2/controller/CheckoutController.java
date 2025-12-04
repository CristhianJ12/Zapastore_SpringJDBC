package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final PedidoService pedidoService;

    public CheckoutController(CartService cartService, PedidoService pedidoService) {
        this.cartService = cartService;
        this.pedidoService = pedidoService;
    }

    @PostMapping("/cliente/checkout")
    public String checkoutPost(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión para realizar la compra.");
            return "redirect:/login";
        }

        try {
            Integer pedidoId = cartService.checkout(cliente);
            redirectAttributes.addFlashAttribute("pedidoIdExitoso", pedidoId);
            session.removeAttribute("carrito");
            session.setAttribute("checkout_status", "SUCCESS");
            return "redirect:/cliente/checkout/success";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/carrito";
        }
    }

    @GetMapping("/cliente/checkout/success")
    public String checkoutSuccess(Model model, HttpSession session) {
        Integer pedidoIdExitoso = (Integer) model.asMap().get("pedidoIdExitoso");
        if (pedidoIdExitoso == null) return "redirect:/cliente/home";

        Optional<Pedido> optionalPedido = pedidoService.findById(pedidoIdExitoso);
        if (optionalPedido.isEmpty()) return "redirect:/cliente/home";

        Pedido pedidoExitoso = optionalPedido.get();
        model.addAttribute("pedidoExitoso", pedidoExitoso);

        return "cliente/compraExitosa";
    }
}
