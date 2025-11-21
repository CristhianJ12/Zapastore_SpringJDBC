package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    Pedido save(Pedido pedido);
    List<Pedido> findByCliente(Usuario cliente);
    Optional<Pedido> findById(Integer id);
    void deleteById(Integer id);
    List<Pedido> findByClienteAndEstado(Usuario cliente, String estado);
}
