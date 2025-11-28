package com.zapastore.zapastore_h2.model.detalle_pedido;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private void actualizarTotal(Integer pedidoId) {
        BigDecimal nuevoTotal = detalleService.calcularSubtotalPorPedido(pedidoId);
        pedidoService.actualizarTotalPagar(pedidoId, nuevoTotal);
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");

        if (cliente == null) {
            return "redirect:/login";
        }

        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        List<ItemCarrito> carrito = new ArrayList<>();
        BigDecimal totalPagar = BigDecimal.ZERO;

        if (!pendientes.isEmpty()) {
            Pedido pedidoPendiente = pendientes.get(0);
            Integer pedidoId = pedidoPendiente.getId();

            List<DetallePedido> detalles = detalleService.listarPorPedido(pedidoId);

            int idCounter = 1;
            for (DetallePedido detalle : detalles) {
                Producto producto = productoService.buscarPorId(detalle.getProductoId());

                if (producto == null) {
                    continue;
                }

                ItemCarrito item = new ItemCarrito(
                        idCounter++,
                        producto,
                        detalle.getCantidad(),
                        detalle.getTalla()
                );
                item.setDetalleId(detalle.getId());
                carrito.add(item);
            }

            totalPagar = detalleService.calcularSubtotalPorPedido(pedidoId);
        }

        session.setAttribute("carrito", carrito);

        int totalItems = carrito.stream().mapToInt(ItemCarrito::getCantidad).sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPagar", totalPagar);

        return "cliente/carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Integer productoId,
                                   @RequestParam Integer talla,
                                   @RequestParam Integer cantidad,
                                   HttpSession session,
                                   RedirectAttributes ra) {

        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) {
            return "redirect:/login";
        }

        Producto producto = productoService.buscarPorId(productoId);
        if (producto == null) {
            ra.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/cliente/home";
        }

        Pedido pedidoPendiente;
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");

        if (pendientes.isEmpty()) {
            pedidoPendiente = new Pedido();
            pedidoPendiente.setCliente(cliente);
            pedidoPendiente.setEstado("Pendiente");
            pedidoPendiente.setFecha(LocalDateTime.now());
            pedidoPendiente.setTotalPagar(BigDecimal.ZERO);
            pedidoPendiente = pedidoService.save(pedidoPendiente);
        } else {
            pedidoPendiente = pendientes.get(0);
        }

        Integer pedidoId = pedidoPendiente.getId();

        DetallePedido detalleExistente = detalleService.buscarPorPedidoProductoYTalla(pedidoId, productoId, talla);

        if (detalleExistente != null) {
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            detalleExistente.setSubtotal(detalleExistente.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleExistente.getCantidad())));
            detalleService.actualizar(detalleExistente);
            ra.addFlashAttribute("mensaje", "Cantidad del producto actualizada en el carrito.");
        } else {
            DetallePedido nuevoDetalle = new DetallePedido();
            nuevoDetalle.setPedidoId(pedidoId);
            nuevoDetalle.setProductoId(productoId);
            nuevoDetalle.setCantidad(cantidad);
            nuevoDetalle.setTalla(talla);
            nuevoDetalle.setPrecioUnitario(producto.getPrecio());
            nuevoDetalle.setNombreProducto(producto.getNombre());
            nuevoDetalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            detalleService.guardar(nuevoDetalle);
            ra.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        }

        actualizarTotal(pedidoId);

        return "redirect:/cliente/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Integer detalleId,
                             @RequestParam Integer cantidad,
                             @RequestParam Integer talla,
                             HttpSession session,
                             RedirectAttributes ra) {

        DetallePedido detalleActual = detalleService.buscarPorId(detalleId);
        if (detalleActual == null) {
            ra.addFlashAttribute("error", "Detalle de producto no encontrado.");
            return "redirect:/cliente/carrito";
        }

        Integer pedidoId = detalleActual.getPedidoId();
        int cantidadOriginal = detalleActual.getCantidad();

        if (!talla.equals(detalleActual.getTalla())) {
            DetallePedido detalleDuplicado = detalleService.buscarPorPedidoProductoYTalla(
                    pedidoId,
                    detalleActual.getProductoId(),
                    talla
            );

            if (detalleDuplicado != null) {
                int nuevaCantidadDuplicado = detalleDuplicado.getCantidad() + cantidadOriginal;
                detalleDuplicado.setCantidad(nuevaCantidadDuplicado);
                detalleDuplicado.setSubtotal(detalleDuplicado.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidadDuplicado)));
                detalleService.actualizar(detalleDuplicado);
                detalleService.eliminar(detalleActual.getId());
                actualizarTotal(pedidoId);
                ra.addFlashAttribute("mensaje", "Producto reagrupado por cambio de talla.");
                return "redirect:/cliente/carrito";
            }
        }

        detalleActual.setCantidad(cantidad);
        detalleActual.setTalla(talla);
        detalleActual.setSubtotal(detalleActual.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));
        detalleService.actualizar(detalleActual);
        actualizarTotal(pedidoId);

        ra.addFlashAttribute("mensaje", "Producto actualizado.");
        return "redirect:/cliente/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer detalleId,
                           HttpSession session,
                           RedirectAttributes ra) {

        DetallePedido detalle = detalleService.buscarPorId(detalleId);
        if (detalle == null) {
            ra.addFlashAttribute("error", "Producto no encontrado para eliminar.");
            return "redirect:/cliente/carrito";
        }

        Integer pedidoId = detalle.getPedidoId();
        detalleService.eliminar(detalleId);
        actualizarTotal(pedidoId);

        ra.addFlashAttribute("mensaje", "Producto eliminado");
        return "redirect:/cliente/carrito";
    }
}