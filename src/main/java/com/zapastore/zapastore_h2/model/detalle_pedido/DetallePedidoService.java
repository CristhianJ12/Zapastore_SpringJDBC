package com.zapastore.zapastore_h2.model.detalle_pedido;

import java.math.BigDecimal;
import java.util.List;

public interface DetallePedidoService {

    List<DetallePedido> listarPorPedido(Integer pedidoId);

    DetallePedido buscarPorId(Integer id);

    void guardar(DetallePedido detalle);

    void actualizar(DetallePedido detalle);

    void eliminar(Integer id);

    DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla);

    BigDecimal calcularSubtotalPorPedido(Integer pedidoId);
}