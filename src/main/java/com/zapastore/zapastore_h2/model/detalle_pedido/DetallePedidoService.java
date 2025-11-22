package com.zapastore.zapastore_h2.model.detalle_pedido;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 💡 SOLUCIÓN CRÍTICA

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional // 👈 Aplica la transacción a todos los métodos
public class DetallePedidoService {

    private final DetallePedidoDAO detalleDao;

    public DetallePedidoService(DetallePedidoDAO detalleDao) {
        this.detalleDao = detalleDao;
    }

    public List<DetallePedido> listarPorPedido(Integer pedidoId) {
        return detalleDao.listarPorPedido(pedidoId);
    }

    public DetallePedido buscarPorId(Integer id) {
        return detalleDao.buscarPorId(id);
    }

    public void guardar(DetallePedido d) {
        detalleDao.guardar(d);
    }

    public void actualizar(DetallePedido d) {
        detalleDao.actualizar(d);
    }

    public void eliminar(Integer id) {
        detalleDao.eliminar(id);
    }

    public DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla) {
        return detalleDao.buscarPorPedidoProductoYTalla(pedidoId, productoId, talla);
    }

    public BigDecimal calcularSubtotalPorPedido(Integer pedidoId) {
        return detalleDao.calcularSubtotalPorPedido(pedidoId);
    }
}