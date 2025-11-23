package com.zapastore.zapastore_h2.model.detalle_pedido;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoDAO detalleDAO;

    public DetallePedidoServiceImpl(DetallePedidoDAO detalleDAO) {
        this.detalleDAO = detalleDAO;
    }

    @Override
    public List<DetallePedido> listarPorPedido(Integer pedidoId) {
        return detalleDAO.listarPorPedido(pedidoId);
    }

    @Override
    public DetallePedido buscarPorId(Integer id) {
        return detalleDAO.buscarPorId(id);
    }

    @Override
    @Transactional
    public void guardar(DetallePedido detalle) {
        detalleDAO.guardar(detalle);
    }

    @Override
    @Transactional
    public void actualizar(DetallePedido detalle) {
        detalleDAO.actualizar(detalle);
    }

    @Override
    @Transactional
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
}