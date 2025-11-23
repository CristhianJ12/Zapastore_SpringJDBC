package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoServiceImpl(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(Integer id) {
        pedidoDAO.deleteById(id);
    }

    @Override
    public List<Pedido> findByClienteAndEstado(Usuario cliente, String estado) {
        return pedidoDAO.findByClienteAndEstado(cliente, estado);
    }

    @Override
    public List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoDAO.findByFechaBetween(inicio, fin);
    }

    @Override
    @Transactional
    public void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal) {
        pedidoDAO.actualizarTotalPagar(pedidoId, nuevoTotal);
    }

    @Override
    @Transactional
    public void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha) {
        pedidoDAO.actualizarEstadoYFecha(pedidoId, nuevoEstado, fecha);
    }

    @Override
    public List<PedidosDTOs.PedidoDiaDTO> obtenerVentasPorDia(LocalDate dia) {
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fin = dia.atTime(23, 59, 59);

        return pedidoDAO.findCompletadosByFechaBetween(inicio, fin).stream()
                .sorted(Comparator.comparing(Pedido::getFecha))
                .map(p -> new PedidosDTOs.PedidoDiaDTO(
                        p.getFecha().getHour() + ":" + String.format("%02d", p.getFecha().getMinute()),
                        p.getTotalPagar()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidosDTOs.DiaSemanaDTO> obtenerVentasPorSemana(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        Map<LocalDate, BigDecimal> ventasReales = pedidoDAO.findCompletadosByFechaBetween(inicioDateTime, finDateTime).stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFecha().toLocalDate(),
                        Collectors.mapping(Pedido::getTotalPagar, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return IntStream.rangeClosed(0, 6)
                .mapToObj(i -> inicio.plusDays(i))
                .map(dia -> {
                    BigDecimal total = ventasReales.getOrDefault(dia, BigDecimal.ZERO);
                    String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es"));
                    return new PedidosDTOs.DiaSemanaDTO(nombreDia, total);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidosDTOs.MesDTO> obtenerVentasPorMes(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        Map<String, BigDecimal> ventasReales = pedidoDAO.findCompletadosByFechaBetween(inicioDateTime, finDateTime).stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            String mes = p.getFecha().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"));
                            String anio = String.valueOf(p.getFecha().getYear());
                            return mes + " " + anio;
                        },
                        Collectors.mapping(Pedido::getTotalPagar, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        List<PedidosDTOs.MesDTO> resultado = new java.util.ArrayList<>();
        LocalDate fechaActual = inicio.withDayOfMonth(1);

        while (!fechaActual.isAfter(fin)) {
            String mesAnioClave = fechaActual.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"))
                    + " " + fechaActual.getYear();

            BigDecimal total = ventasReales.getOrDefault(mesAnioClave, BigDecimal.ZERO);
            resultado.add(new PedidosDTOs.MesDTO(mesAnioClave, total));

            fechaActual = fechaActual.plusMonths(1);
        }

        return resultado;
    }

    @Override
    public BigDecimal calcularTotalVentasPorRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);
        return pedidoDAO.calcularTotalVentasPorFecha(inicioDateTime, finDateTime);
    }

    @Override
    public int contarPedidosCompletadosPorRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);
        return pedidoDAO.contarPedidosCompletadosPorFecha(inicioDateTime, finDateTime);
    }
}