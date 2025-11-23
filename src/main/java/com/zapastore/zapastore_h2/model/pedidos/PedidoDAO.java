package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    Pedido save(Pedido pedido);

    List<Pedido> findByCliente(Usuario cliente);

    Optional<Pedido> findById(Integer id);

    void deleteById(Integer id);

    List<Pedido> findByClienteAndEstado(Usuario cliente, String estado);

    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal);

    void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha);

    List<Pedido> findCompletadosByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    BigDecimal calcularTotalVentasPorFecha(LocalDateTime inicio, LocalDateTime fin);

    int contarPedidosCompletadosPorFecha(LocalDateTime inicio, LocalDateTime fin);
}