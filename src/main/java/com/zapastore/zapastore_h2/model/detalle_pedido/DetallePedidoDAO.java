package com.zapastore.zapastore_h2.model.detalle_pedido;

import java.math.BigDecimal;
import java.util.List;

public interface DetallePedidoDAO {

    // Métodos CRUD básicos
    List<DetallePedido> listarPorPedido(Integer pedidoId);
    DetallePedido buscarPorId(Integer id);
    void guardar(DetallePedido d);
    void actualizar(DetallePedido d);
    void eliminar(Integer id);

    // Métodos de negocio específicos
    DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla);
    BigDecimal calcularSubtotalPorPedido(Integer pedidoId);
}