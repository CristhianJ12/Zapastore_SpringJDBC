package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 NUEVO
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoServiceImpl(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    @Transactional // 👈 Aplicar para INSERT y UPDATE (Checkout)
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
    @Transactional // Aplicar para DELETE
    public void deleteById(Integer id) {
        pedidoDAO.deleteById(id);
    }

    @Override
    public List<Pedido> findByClienteAndEstado(Usuario cliente, String estado) {
        return pedidoDAO.findByClienteAndEstado(cliente, estado);
    }

    @Override
    @Transactional // 👈 Aplicar para UPDATE del total (Carrito)
    public void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal) {
        ((PedidoRepository) pedidoDAO).actualizarTotalPagar(pedidoId, nuevoTotal);
    }

    @Override
    @Transactional
    public void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha) {
        // 💡 ACCESO AL REPOSITORIO CON EL CASTING
        ((PedidoRepository) pedidoDAO).actualizarEstadoYFecha(pedidoId, nuevoEstado, fecha);
    }
}