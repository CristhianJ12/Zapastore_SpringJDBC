package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedido;
import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedidoService;
import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;
    private final ProductoService productoService;

    public CheckoutController(PedidoService pedidoService,
                              DetallePedidoService detallePedidoService,
                              ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
        this.productoService = productoService;
    }

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
        Integer pedidoId = pedido.getId();

        // VALIDACIÓN CRÍTICA: Verificar si hay productos inactivos en el pedido
        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(pedidoId);
        boolean hayProductosInactivos = false;

        for (DetallePedido detalle : detalles) {
            Producto producto = productoService.buscarPorId(detalle.getProductoId());

            if (producto == null || "Inactivo".equalsIgnoreCase(producto.getEstado())) {
                hayProductosInactivos = true;
                break;
            }
        }

        // Si hay productos inactivos, bloquear el checkout
        if (hayProductosInactivos) {
            redirectAttributes.addFlashAttribute("error",
                    "⚠️ No puedes completar la compra porque tienes productos descontinuados en tu carrito. " +
                            "Por favor, elimínalos antes de continuar.");
            return "redirect:/cliente/carrito";
        }

        // Si todo está bien, completar la compra
        pedidoService.actualizarEstadoYFecha(pedidoId, "Completado", LocalDateTime.now());

        session.removeAttribute("carrito");

        redirectAttributes.addFlashAttribute("pedidoIdExitoso", pedidoId);
        return "redirect:/cliente/checkout/success";
    }

    @GetMapping("/cliente/checkout/success")
    public String checkoutSuccess(Model model, HttpSession session) {
        Integer pedidoIdExitoso = (Integer) model.asMap().get("pedidoIdExitoso");

        if (pedidoIdExitoso == null) {
            return "redirect:/cliente/home";
        }

        Optional<Pedido> optionalPedido = pedidoService.findById(pedidoIdExitoso);

        if (optionalPedido.isEmpty()) {
            return "redirect:/cliente/home";
        }

        Pedido pedidoExitoso = optionalPedido.get();
        pedidoExitoso.setDetalles(detallePedidoService.listarPorPedido(pedidoIdExitoso));

        model.addAttribute("pedidoExitoso", pedidoExitoso);
        session.setAttribute("checkout_status", "SUCCESS");

        return "cliente/compraExitosa";
    }
}