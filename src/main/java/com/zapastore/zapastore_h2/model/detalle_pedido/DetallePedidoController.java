package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.detalle_pedido.ItemCarrito;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cliente/carrito")
public class DetallePedidoController {

    private final CartService cartService;

    public DetallePedidoController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) return "redirect:/login";

        List<ItemCarrito> carrito = cartService.getCartItems(cliente);
        int totalItems = carrito.stream().mapToInt(ItemCarrito::getCantidad).sum();
        BigDecimal totalPagar = cartService.getCartTotal(cliente);

        session.setAttribute("carrito", carrito);
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPagar", totalPagar);
        model.addAttribute("hayProductosInactivos", carrito.stream().anyMatch(ItemCarrito::isProductoInactivo));

        return "cliente/carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Integer productoId,
                                   @RequestParam Integer talla,
                                   @RequestParam Integer cantidad,
                                   HttpSession session,
                                   RedirectAttributes ra) {

        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) return "redirect:/login";

        try {
            cartService.addToCart(cliente, productoId, talla, cantidad);
            ra.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Integer detalleId,
                             @RequestParam Integer cantidad,
                             @RequestParam Integer talla,
                             HttpSession session,
                             RedirectAttributes ra) {
        try {
            cartService.updateCartItem(detalleId, cantidad, talla);
            ra.addFlashAttribute("mensaje", "Producto actualizado.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer detalleId,
                           HttpSession session,
                           RedirectAttributes ra) {
        try {
            cartService.removeCartItem(detalleId);
            ra.addFlashAttribute("mensaje", "Producto eliminado");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/carrito";
    }
}

