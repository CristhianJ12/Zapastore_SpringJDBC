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

    // Método auxiliar para actualizar el total del pedido en BD
    private void actualizarTotal(Integer pedidoId) {
        BigDecimal nuevoTotal = detalleService.calcularSubtotalPorPedido(pedidoId);
        pedidoService.actualizarTotalPagar(pedidoId, nuevoTotal);
        System.out.println("DEBUG: Pedido ID " + pedidoId + " Total actualizado a: " + nuevoTotal);
    }

    // ------------------------------
    // 1. VER CARRITO
    // ------------------------------
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");

        if (cliente == null) {
            System.out.println("ERROR CRÍTICO: Sesión de usuario NULA. Redirigiendo a login.");
            return "redirect:/login";
        }

        System.out.println("DEBUG CONTROLLER: Intentando ver carrito para ID: " + cliente.getIdUsuario());

        // Buscar pedido pendiente del cliente
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");

        List<ItemCarrito> carrito = new ArrayList<>();
        BigDecimal totalPagar = BigDecimal.ZERO;

        System.out.println("DEBUG CONTROLLER: Pedidos pendientes encontrados: " + pendientes.size());

        if (!pendientes.isEmpty()) {
            Pedido pedidoPendiente = pendientes.get(0);
            Integer pedidoId = pedidoPendiente.getId();

            System.out.println("DEBUG CONTROLLER: Usando Pedido Pendiente ID: " + pedidoId);

            // Reconstruir carrito desde detalles del pedido
            List<DetallePedido> detalles = detalleService.listarPorPedido(pedidoId);

            System.out.println("DEBUG CONTROLLER: Detalles encontrados para Pedido " + pedidoId + ": " + detalles.size());

            int idCounter = 1;
            for (DetallePedido detalle : detalles) {

                // 💡 LOG CRÍTICO AÑADIDO: Verificación de ID de Detalle y ID de Producto
                System.out.println("DEBUG CONTROLLER: Procesando Detalle ID: " + detalle.getId() + ", Producto ID: " + detalle.getProductoId());

                // 💡 CORRECCIÓN CRÍTICA: Debemos cargar el objeto Producto completo
                Producto producto = productoService.buscarPorId(detalle.getProductoId());

                if (producto == null) {
                    System.out.println("ADVERTENCIA CONTROLLER: Producto (ID: " + detalle.getProductoId() + ") es NULO. Fila de detalle ignorada.");
                    continue; // Ignorar detalles huérfanos
                }

                // 💡 LOG DE ÉXITO AÑADIDO: Confirmar que el producto fue cargado
                System.out.println("DEBUG CONTROLLER: Producto cargado: " + producto.getNombre());

                ItemCarrito item = new ItemCarrito(
                        idCounter++,
                        producto, // <-- Usamos el producto que acabamos de cargar
                        detalle.getCantidad(),
                        detalle.getTalla()
                );
                // 👈 Guardamos el ID de BD
                item.setDetalleId(detalle.getId());
                carrito.add(item);
            }
            // Recalcular el total desde la BD (para asegurar consistencia)
            totalPagar = detalleService.calcularSubtotalPorPedido(pedidoId);
        } else {
            System.out.println("ADVERTENCIA CONTROLLER: No se encontró ningún Pedido Pendiente. Carrito vacío.");
        }

        // Guardar carrito en sesión (solo la representación para la vista)
        session.setAttribute("carrito", carrito);

        int totalItems = carrito.stream().mapToInt(ItemCarrito::getCantidad).sum();

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
                                   HttpSession session,
                                   RedirectAttributes ra) {

        Usuario cliente = (Usuario) session.getAttribute("usuarioSesion");
        if (cliente == null) return "redirect:/login";

        System.out.println("DEBUG CONTROLLER: Agregando Producto ID: " + productoId + ", Talla: " + talla + ", Cantidad: " + cantidad + " para Cliente: " + cliente.getIdUsuario());

        Producto producto = productoService.buscarPorId(productoId);
        if (producto == null) {
            System.out.println("ERROR CRÍTICO: Producto con ID " + productoId + " es NULL. Fallo en ProductoService.");
            ra.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/cliente/home";
        }

        System.out.println("DEBUG CONTROLLER: Producto encontrado: " + producto.getNombre() + " | Precio: " + producto.getPrecio());


        // 1. Obtener o Crear Pedido Pendiente
        Pedido pedidoPendiente;
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");

        System.out.println("DEBUG CONTROLLER: Pendientes encontrados antes de agregar: " + pendientes.size());

        if (pendientes.isEmpty()) {
            // No existe un pedido pendiente, CREAR UNO NUEVO
            pedidoPendiente = new Pedido();
            pedidoPendiente.setCliente(cliente);
            pedidoPendiente.setEstado("Pendiente");
            pedidoPendiente.setFecha(LocalDateTime.now());
            pedidoPendiente.setTotalPagar(BigDecimal.ZERO);

            System.out.println("DEBUG CONTROLLER: Creando nuevo Pedido Pendiente...");
            pedidoPendiente = pedidoService.save(pedidoPendiente);
            System.out.println("DEBUG CONTROLLER: Nuevo Pedido creado con ID: " + pedidoPendiente.getId());

        } else {
            pedidoPendiente = pendientes.get(0);
            System.out.println("DEBUG CONTROLLER: Usando Pedido Pendiente existente ID: " + pedidoPendiente.getId());
        }

        Integer pedidoId = pedidoPendiente.getId();

        // 2. Control de Duplicados
        DetallePedido detalleExistente = detalleService
                .buscarPorPedidoProductoYTalla(pedidoId, productoId, talla);

        if (detalleExistente != null) {
            // Es un duplicado: Aumentar Cantidad en BD
            System.out.println("DEBUG CONTROLLER: Detalle existente (ID: " + detalleExistente.getId() + "). Sumando cantidad.");
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            detalleExistente.setSubtotal(detalleExistente.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleExistente.getCantidad())));
            detalleService.actualizar(detalleExistente);
            ra.addFlashAttribute("mensaje", "Cantidad del producto actualizada en el carrito.");

        } else {
            // Producto nuevo: Crear nuevo DetallePedido en BD
            System.out.println("DEBUG CONTROLLER: Creando nuevo Detalle de Pedido.");
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

        // 3. Recalcular y actualizar el total del Pedido
        actualizarTotal(pedidoId);

        return "redirect:/cliente/carrito";
    }

    // ------------------------------
    // 3. ACTUALIZAR PRODUCTO (Se mantiene sin logs adicionales por ser dependiente)
    // ------------------------------
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

        // Almacenamos la cantidad original, en caso de que necesitemos fusionar.
        int cantidadOriginal = detalleActual.getCantidad();

        // 1. Verificar si el cambio de talla crea un duplicado con otro detalle
        if (!talla.equals(detalleActual.getTalla())) {

            DetallePedido detalleDuplicado = detalleService
                    .buscarPorPedidoProductoYTalla(
                            pedidoId,
                            detalleActual.getProductoId(),
                            talla // Busca la nueva talla
                    );

            if (detalleDuplicado != null) {
                // Hay duplicado: FUSIONAR Y ELIMINAR EL DETALLE ACTUAL

                // 💡 LÓGICA CORREGIDA: Sumar la cantidad original del detalle actual (antes de la edición)
                // a la cantidad del detalle duplicado existente.
                int nuevaCantidadDuplicado = detalleDuplicado.getCantidad() + cantidadOriginal;

                detalleDuplicado.setCantidad(nuevaCantidadDuplicado);
                detalleDuplicado.setSubtotal(detalleDuplicado.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidadDuplicado)));
                detalleService.actualizar(detalleDuplicado);

                detalleService.eliminar(detalleActual.getId()); // Elimina el detalle original (ID 3 en el ejemplo)

                actualizarTotal(pedidoId);
                ra.addFlashAttribute("mensaje", "Producto reagrupado por cambio de talla.");
                return "redirect:/cliente/carrito";
            }
        }

        // 2. Si no hubo fusión (solo cambio de cantidad, o cambio de talla sin conflicto):

        // Actualizar con la nueva 'cantidad' y 'talla' proporcionadas por el formulario
        detalleActual.setCantidad(cantidad);
        detalleActual.setTalla(talla);
        detalleActual.setSubtotal(detalleActual.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));

        detalleService.actualizar(detalleActual);

        // 3. Recalcular y actualizar el total del Pedido
        actualizarTotal(pedidoId);

        ra.addFlashAttribute("mensaje", "Producto actualizado.");
        return "redirect:/cliente/carrito";
    }

    // ------------------------------
    // 4. ELIMINAR PRODUCTO (Se mantiene sin logs adicionales por ser dependiente)
    // ------------------------------
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer detalleId, // ID real del detalle en BD
                           HttpSession session,
                           RedirectAttributes ra) {

        DetallePedido detalle = detalleService.buscarPorId(detalleId);
        if (detalle == null) {
            ra.addFlashAttribute("error", "Producto no encontrado para eliminar.");
            return "redirect:/cliente/carrito";
        }

        Integer pedidoId = detalle.getPedidoId();

        detalleService.eliminar(detalleId);

        // Recalcular y actualizar el total del Pedido
        actualizarTotal(pedidoId);

        ra.addFlashAttribute("mensaje", "Producto eliminado");
        return "redirect:/cliente/carrito";
    }
}