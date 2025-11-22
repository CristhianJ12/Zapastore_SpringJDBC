/*package com.zapastore.zapastore_h2.model.detalle_pedido;

import com.zapastore.zapastore_h2.model.producto.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 NUEVO
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoDAO detalleDAO;
    private final ProductoService productoService;

    public DetallePedidoServiceImpl(DetallePedidoDAO detalleDAO, ProductoService productoService) {
        this.detalleDAO = detalleDAO;
        this.productoService = productoService;
    }

    // ... (Métodos listarPorPedido, buscarPorId, etc. que solo leen) ...

    @Override
    public List<DetallePedido> listarPorPedido(Integer pedidoId) {
        return List.of();
    }

    @Override
    public DetallePedido buscarPorId(Integer id) {
        return null;
    }

    @Override
    @Transactional // 👈 Aplicar para INSERT (Agregar al carrito)
    public void guardar(DetallePedido detalle) {
        detalleDAO.guardar(detalle);
    }

    @Override
    @Transactional // 👈 Aplicar para UPDATE (Editar cantidad/talla)
    public void actualizar(DetallePedido detalle) {
        detalleDAO.actualizar(detalle);
    }

    @Override
    @Transactional // 👈 Aplicar para DELETE (Eliminar del carrito)
    public void eliminar(Integer id) {
        detalleDAO.eliminar(id);
    }

    @Override
    public DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla) {
        return detalleDAO.buscarPorPedidoProductoYTalla(pedidoId, productoId, talla);
    }

    @Override
    public BigDecimal calcularSubtotalPorPedido(Integer pedidoId) {
        return detalleDAO.calcularSubtotalPorPedido(pedidoId);
    }
}*/