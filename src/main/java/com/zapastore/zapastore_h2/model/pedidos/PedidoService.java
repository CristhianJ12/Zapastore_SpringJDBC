package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {

    Pedido save(Pedido pedido);

    List<Pedido> findByCliente(Usuario cliente);

    Optional<Pedido> findById(Integer id);

    void deleteById(Integer id);

    List<Pedido> findByClienteAndEstado(Usuario cliente, String estado);

    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal);

    void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha);

    List<PedidosDTOs.PedidoDiaDTO> obtenerVentasPorDia(LocalDate dia);

    List<PedidosDTOs.DiaSemanaDTO> obtenerVentasPorSemana(LocalDate inicio, LocalDate fin);

    List<PedidosDTOs.MesDTO> obtenerVentasPorMes(LocalDate inicio, LocalDate fin);

    BigDecimal calcularTotalVentasPorRango(LocalDate inicio, LocalDate fin);

    int contarPedidosCompletadosPorRango(LocalDate inicio, LocalDate fin);
}