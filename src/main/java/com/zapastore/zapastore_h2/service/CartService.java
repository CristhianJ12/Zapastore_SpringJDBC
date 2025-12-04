package com.zapastore.zapastore_h2.service;

import com.zapastore.zapastore_h2.model.detalle_pedido.ItemCarrito;
import com.zapastore.zapastore_h2.model.pedidos.Pedido;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

public interface CartService {

    List<ItemCarrito> getCartItems(Usuario cliente);

    Integer addToCart(Usuario cliente, Integer productoId, Integer talla, Integer cantidad) throws IllegalArgumentException;

    void updateCartItem(Integer detalleId, Integer cantidad, Integer talla) throws IllegalArgumentException;

    void removeCartItem(Integer detalleId) throws IllegalArgumentException;

    Integer checkout(Usuario cliente) throws IllegalStateException;
    java.math.BigDecimal getCartTotal(Usuario cliente);
}
