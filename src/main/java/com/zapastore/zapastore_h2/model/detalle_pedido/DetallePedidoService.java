package com.zapastore.zapastore_h2.model.detalle_pedido;

import com.zapastore.zapastore_h2.model.producto.Producto;

import java.util.List;

public interface DetallePedidoService {

    List<DetallePedido> listarPorPedido(Integer pedidoId);

    DetallePedido buscarPorId(Integer id);

    void guardar(DetallePedido detalle);

    void actualizar(DetallePedido detalle);

    void eliminar(Integer id);

}