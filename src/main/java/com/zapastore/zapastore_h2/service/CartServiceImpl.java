package com.zapastore.zapastore_h2.service;

import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedido;
import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedidoService;
import com.zapastore.zapastore_h2.model.detalle_pedido.ItemCarrito;
import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final DetallePedidoService detalleService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public CartServiceImpl(DetallePedidoService detalleService,
                           PedidoService pedidoService,
                           ProductoService productoService) {
        this.detalleService = detalleService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @Override
    public List<ItemCarrito> getCartItems(Usuario cliente) {
        if (cliente == null) return List.of();

        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pendientes.isEmpty()) return List.of();

        Pedido pedido = pendientes.get(0);
        Integer pedidoId = pedido.getId();

        List<DetallePedido> detalles = detalleService.listarPorPedido(pedidoId);
        List<ItemCarrito> carrito = new ArrayList<>();

        int idCounter = 1;
        for (DetallePedido d : detalles) {
            Producto prod = productoService.buscarPorId(d.getProductoId());
            if (prod == null) continue;

            ItemCarrito item = new ItemCarrito(idCounter++, prod, d.getCantidad(), d.getTalla());
            item.setDetalleId(d.getId());
            // marcar inactivo si aplica
            item.setProductoInactivo(prod.getEstado() == null ? false : "Inactivo".equalsIgnoreCase(prod.getEstado()));
            carrito.add(item);
        }
        return carrito;
    }

    @Override
    @Transactional
    public Integer addToCart(Usuario cliente, Integer productoId, Integer talla, Integer cantidad) throws IllegalArgumentException {
        if (cliente == null) throw new IllegalArgumentException("Debe iniciar sesión.");
        Producto producto = productoService.buscarPorId(productoId);
        if (producto == null) throw new IllegalArgumentException("Producto no encontrado.");
        if ("Inactivo".equalsIgnoreCase(producto.getEstado())) throw new IllegalArgumentException("Producto inactivo.");

        Pedido pedidoPendiente;
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pendientes.isEmpty()) {
            Pedido nuevo = new Pedido();
            nuevo.setCliente(cliente);
            nuevo.setEstado("Pendiente");
            nuevo.setFecha(LocalDateTime.now());
            nuevo.setTotalPagar(BigDecimal.ZERO);
            nuevo.setCostoEnvio(BigDecimal.ZERO);
            pedidoPendiente = pedidoService.save(nuevo);
        } else {
            pedidoPendiente = pendientes.get(0);
        }

        Integer pedidoId = pedidoPendiente.getId();

        DetallePedido detalleExistente = detalleService.buscarPorPedidoProductoYTalla(pedidoId, productoId, talla);

        if (detalleExistente != null) {
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            detalleExistente.setSubtotal(detalleExistente.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleExistente.getCantidad())));
            detalleService.actualizar(detalleExistente);
        } else {
            DetallePedido nuevo = new DetallePedido();
            nuevo.setPedidoId(pedidoId);
            nuevo.setProductoId(productoId);
            nuevo.setCantidad(cantidad);
            nuevo.setTalla(talla);
            nuevo.setPrecioUnitario(producto.getPrecio());
            nuevo.setNombreProducto(producto.getNombre());
            nuevo.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            detalleService.guardar(nuevo);
        }

        // Actualizar total en pedido (transaccional)
        BigDecimal nuevoTotal = detalleService.calcularSubtotalPorPedido(pedidoId);
        pedidoService.actualizarTotalPagar(pedidoId, nuevoTotal);

        return pedidoId;
    }

    @Override
    @Transactional
    public void updateCartItem(Integer detalleId, Integer cantidad, Integer talla) throws IllegalArgumentException {
        DetallePedido detalle = detalleService.buscarPorId(detalleId);
        if (detalle == null) throw new IllegalArgumentException("Detalle no encontrado.");

        Producto producto = productoService.buscarPorId(detalle.getProductoId());
        if (producto == null || "Inactivo".equalsIgnoreCase(producto.getEstado())) {
            throw new IllegalArgumentException("Producto descontinuado. Elimine el item.");
        }

        Integer pedidoId = detalle.getPedidoId();
        int cantidadOriginal = detalle.getCantidad();

        if (!talla.equals(detalle.getTalla())) {
            DetallePedido duplicado = detalleService.buscarPorPedidoProductoYTalla(pedidoId, detalle.getProductoId(), talla);
            if (duplicado != null) {
                int nuevaCantidad = duplicado.getCantidad() + cantidad;
                duplicado.setCantidad(nuevaCantidad);
                duplicado.setSubtotal(duplicado.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad)));
                detalleService.actualizar(duplicado);
                detalleService.eliminar(detalle.getId());
                pedidoService.actualizarTotalPagar(pedidoId, detalleService.calcularSubtotalPorPedido(pedidoId));
                return;
            }
        }

        detalle.setCantidad(cantidad);
        detalle.setTalla(talla);
        detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));
        detalleService.actualizar(detalle);
        pedidoService.actualizarTotalPagar(pedidoId, detalleService.calcularSubtotalPorPedido(pedidoId));
    }

    @Override
    @Transactional
    public void removeCartItem(Integer detalleId) throws IllegalArgumentException {
        DetallePedido detalle = detalleService.buscarPorId(detalleId);
        if (detalle == null) throw new IllegalArgumentException("Detalle no encontrado.");
        Integer pedidoId = detalle.getPedidoId();
        detalleService.eliminar(detalleId);
        pedidoService.actualizarTotalPagar(pedidoId, detalleService.calcularSubtotalPorPedido(pedidoId));
    }

    @Override
    @Transactional
    public Integer checkout(Usuario cliente) throws IllegalStateException {
        if (cliente == null) throw new IllegalStateException("Debe iniciar sesión.");

        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pendientes.isEmpty()) throw new IllegalStateException("No hay pedidos pendientes.");

        Pedido pedido = pendientes.get(0);
        Integer pedidoId = pedido.getId();

        // Validar productos activos
        List<DetallePedido> detalles = detalleService.listarPorPedido(pedidoId);
        for (DetallePedido d : detalles) {
            Producto p = productoService.buscarPorId(d.getProductoId());
            if (p == null || "Inactivo".equalsIgnoreCase(p.getEstado())) {
                throw new IllegalStateException("Hay productos descontinuados en el carrito.");
            }
        }

        pedidoService.actualizarEstadoYFecha(pedidoId, "Completado", LocalDateTime.now());
        return pedidoId;
    }

    @Override
    public BigDecimal getCartTotal(Usuario cliente) {
        List<Pedido> pendientes = pedidoService.findByClienteAndEstado(cliente, "Pendiente");
        if (pendientes.isEmpty()) return BigDecimal.ZERO;
        return detalleService.calcularSubtotalPorPedido(pendientes.get(0).getId());
    }
}
