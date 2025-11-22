package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.math.BigDecimal; // Importar
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido save(Pedido pedido);
    List<Pedido> findByCliente(Usuario cliente);
    Optional<Pedido> findById(Integer id);
    void deleteById(Integer id);
    List<Pedido> findByClienteAndEstado(Usuario cliente, String estado);

    // 👈 NUEVO: Para actualizar el campo total_pagar
    void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal);
    void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha);
}