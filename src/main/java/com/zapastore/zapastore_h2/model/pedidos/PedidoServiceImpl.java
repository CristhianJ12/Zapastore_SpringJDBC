package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoServiceImpl(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoDAO.save(pedido);
    }

    @Override
    public List<Pedido> findByCliente(Usuario cliente) {
        return pedidoDAO.findByCliente(cliente);
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        return pedidoDAO.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        pedidoDAO.deleteById(id);
    }

    @Override
    public List<Pedido> findByClienteAndEstado(Usuario cliente, String estado) {
        return pedidoDAO.findByClienteAndEstado(cliente, estado);
    }
}
