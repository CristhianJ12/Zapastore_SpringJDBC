package com.zapastore.zapastore_h2.model.detalle_pedido;

import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente/carrito")
public class DetallePedidoController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final DetallePedidoService detalleService;

    public DetallePedidoController(ProductoService productoService,
                                   PedidoService pedidoService,
                                   DetallePedidoService detalleService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.detalleService = detalleService;
    }

    // ------------------------------
    // 1. VER CARRITO
    // ------------------------------
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) return "redirect:/login";

        // Buscar pedido pendiente del cliente
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");

        List<ItemCarrito> carrito = new ArrayList<>();
        if (!pendientes.isEmpty()) {
            Pedido pedidoPendiente = pendientes.get(0);

            // Reconstruir carrito desde detalles del pedido
            List<DetallePedido> detalles = detalleService.listarPorPedido(pedidoPendiente.getId());
            int idCounter = 1;
            for (DetallePedido detalle : detalles) {
                ItemCarrito item = new ItemCarrito(
                        idCounter++,
                        detalle.getProducto(),
                        detalle.getCantidad(),
                        detalle.getTalla()
                );
                carrito.add(item);
            }
        }

        // Guardar carrito en sesión
        session.setAttribute("carrito", carrito);

        // Totales
        int totalItems = carrito.stream().mapToInt(ItemCarrito::getCantidad).sum();
        BigDecimal totalPagar = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPagar", totalPagar);

        return "cliente/carrito";
    }

    // ------------------------------
    // 2. AGREGAR PRODUCTO
    // ------------------------------
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Integer productoId,
                                   @RequestParam Integer talla,
                                   @RequestParam Integer cantidad,
                                   HttpSession session) {

        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) return "redirect:/login";

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        Producto producto = productoService.buscarPorId(productoId);

        // Pedido pendiente
        Pedido pedidoPendiente;
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pendientes.isEmpty()) {
            pedidoPendiente = new Pedido();
            pedidoPendiente.setCliente(cliente);
            pedidoPendiente.setEstado("Pendiente");
            pedidoPendiente.setFecha(LocalDateTime.now());
            pedidoPendiente.setTotalPagar(BigDecimal.ZERO);
            pedidoService.save(pedidoPendiente);
        } else {
            pedidoPendiente = pendientes.get(0);
        }

        // Verificar duplicados
        Optional<ItemCarrito> existente = carrito.stream()
                .filter(i -> i.getProducto().getId().equals(productoId) && i.getTalla().equals(talla))
                .findFirst();
        return "redirect:/cliente/carrito";
    }

    // ------------------------------
    // 3. ACTUALIZAR PRODUCTO
    // ------------------------------
    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Integer detalleId,
                             @RequestParam Integer cantidad,
                             @RequestParam Integer talla,
                             HttpSession session,
                             RedirectAttributes ra) {

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            ra.addFlashAttribute("error", "Carrito vacío");
            return "redirect:/cliente/carrito";
        }

        ItemCarrito item = carrito.stream()
                .filter(i -> i.getId().equals(detalleId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            ra.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/cliente/carrito";
        }

        // Verificar si al cambiar talla se convierte en duplicado
        Optional<ItemCarrito> duplicado = carrito.stream()
                .filter(i -> !i.getId().equals(detalleId) &&
                        i.getProducto().getId().equals(item.getProducto().getId()) &&
                        i.getTalla().equals(talla))
                .findFirst();

        if (duplicado.isPresent()) {
            ItemCarrito otro = duplicado.get();
            otro.setCantidad(otro.getCantidad() + cantidad);
            carrito.remove(item);
        } else {
            item.setCantidad(cantidad);
            item.setTalla(talla);
        }

        // Recalcular subtotales
        carrito.forEach(ItemCarrito::calcularSubtotal);

        session.setAttribute("carrito", carrito);
        ra.addFlashAttribute("mensaje", "Producto actualizado");
        return "redirect:/cliente/carrito";
    }

    // ------------------------------
    // 4. ELIMINAR PRODUCTO
    // ------------------------------
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer detalleId,
                           HttpSession session,
                           RedirectAttributes ra) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(i -> i.getId().equals(detalleId));
        }
        session.setAttribute("carrito", carrito);
        ra.addFlashAttribute("mensaje", "Producto eliminado");
        return "redirect:/cliente/carrito";
    }
}
