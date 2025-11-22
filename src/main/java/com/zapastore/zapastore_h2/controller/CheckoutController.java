package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedidoService; // 👈 Importar
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
import java.util.Optional;

@Controller
public class CheckoutController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService; // 👈 Inyectar servicio de Detalle

    public CheckoutController(PedidoService pedidoService, DetallePedidoService detallePedidoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService; // 👈 Inicializar
    }

    // -------------------------------
    // POST: Finalizar Compra
    // -------------------------------
    @PostMapping("/cliente/checkout")
    public String checkoutPost(HttpSession session, RedirectAttributes redirectAttributes) {
        // ... (Lógica de verificación de usuario y pedidos pendientes sin cambios) ...
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

        // Actualizar estado y fecha
        pedidoService.actualizarEstadoYFecha(pedidoId, "Completado", LocalDateTime.now());

        // Limpiar carrito de la sesión
        session.removeAttribute("carrito");

        // 💡 CRÍTICO: Usamos 'flash' para enviar el ID del pedido solo por una vez.
        redirectAttributes.addFlashAttribute("pedidoIdExitoso", pedidoId);
        return "redirect:/cliente/checkout/success";
    }

    // -------------------------------
    // GET: Página de éxito (Manejo de recarga y obtención de detalles)
    // -------------------------------
    @GetMapping("/cliente/checkout/success")
    public String checkoutSuccess(Model model, HttpSession session) {

        Integer pedidoIdExitoso = (Integer) model.asMap().get("pedidoIdExitoso");

        // 1. Prevenir recarga/acceso directo (CRÍTICO)
        // El 'pedidoIdExitoso' solo existe en el Model si fue pasado por RedirectAttributes (POST-Redirect-GET).
        if (pedidoIdExitoso == null) {
            // Si no hay ID en el modelo, significa que el usuario intentó recargar (F5) o acceder
            // directamente, o ya se ha consumido el FlashAttribute.
            System.out.println("ADVERTENCIA: Intento de recarga o acceso directo a /success. Redirigiendo a home.");
            return "redirect:/cliente/home";
        }

        // 2. Cargar el Pedido y sus Detalles
        Optional<Pedido> optionalPedido = pedidoService.findById(pedidoIdExitoso);

        if (optionalPedido.isEmpty()) {
            // Esto no debería pasar si la transacción fue exitosa
            return "redirect:/cliente/home";
        }

        Pedido pedidoExitoso = optionalPedido.get();

        // 💡 Obtener los detalles del pedido
        pedidoExitoso.setDetalles(detallePedidoService.listarPorPedido(pedidoIdExitoso));

        // 3. Pasar el objeto Pedido completo al JSP
        model.addAttribute("pedidoExitoso", pedidoExitoso);

        // 4. Establecer un atributo de sesión para prevenir el "Back" del navegador
        // Usamos un token simple que borraremos cuando salgamos de la página de éxito.
        session.setAttribute("checkout_status", "SUCCESS");

        return "cliente/compraExitosa";
    }
}