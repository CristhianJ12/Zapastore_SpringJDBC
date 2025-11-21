package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.pedidos.dto.PedidosDTOs.DiaSemanaDTO;
import com.zapastore.zapastore_h2.model.pedidos.dto.PedidosDTOs.MesDTO;
import com.zapastore.zapastore_h2.model.pedidos.dto.PedidosDTOs.PedidoDiaDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MetricaService {

    private final PedidoRepository pedidoRepository;

    public MetricaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Ventas por Día (SIN CAMBIOS)
    public List<PedidoDiaDTO> pedidosPorDia(LocalDate dia) {
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fin = dia.atTime(23, 59, 59);

        return pedidoRepository.findByFechaBetween(inicio, fin).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .sorted(Comparator.comparing(Pedido::getFecha))
                .map(p -> new PedidoDiaDTO(
                        p.getFecha().getHour() + ":" + String.format("%02d", p.getFecha().getMinute()),
                        p.getTotalPagar()
                ))
                .collect(Collectors.toList());
    }

    // Ventas por Semana (MODIFICADA: Asegura los 7 días, rellena con 0)
    public List<DiaSemanaDTO> ventasPorDiaEnRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        // 1. Obtener todas las ventas COMPLETADAS del rango y agruparlas por fecha
        Map<LocalDate, BigDecimal> ventasReales = pedidoRepository.findByFechaBetween(inicioDateTime, finDateTime).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .collect(Collectors.groupingBy(
                        p -> p.getFecha().toLocalDate(),
                        Collectors.mapping(Pedido::getTotalPagar, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        // 2. Generar el rango de días (asumimos 7 días) y rellenar con ceros si no hay ventas
        List<DiaSemanaDTO> resultado = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> inicio.plusDays(i))
                .map(dia -> {
                    BigDecimal total = ventasReales.getOrDefault(dia, BigDecimal.ZERO);
                    // Usar el nombre corto del día de la semana (ej. 'Dom', 'Lun')
                    String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es"));
                    return new DiaSemanaDTO(nombreDia, total);
                })
                .collect(Collectors.toList());

        return resultado;
    }

    // Ventas por Mes (MODIFICADA: Asegura todos los meses del rango, rellena con 0)
    public List<MesDTO> ventasPorMesEnRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        // 1. Obtener todas las ventas COMPLETADAS del rango y agruparlas por MES y AÑO
        Map<String, BigDecimal> ventasReales = pedidoRepository.findByFechaBetween(inicioDateTime, finDateTime).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .collect(Collectors.groupingBy(
                        p -> {
                            String mes = p.getFecha().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"));
                            String anio = String.valueOf(p.getFecha().getYear());
                            return mes + " " + anio;
                        },
                        Collectors.mapping(Pedido::getTotalPagar, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        // 2. Generar el rango completo de meses y rellenar con ceros
        List<MesDTO> resultado = new java.util.ArrayList<>();
        LocalDate fechaActual = inicio.withDayOfMonth(1); // Empezamos al inicio del mes

        while (!fechaActual.isAfter(fin)) {
            String mesAnioClave = fechaActual.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"))
                    + " " + fechaActual.getYear();

            BigDecimal total = ventasReales.getOrDefault(mesAnioClave, BigDecimal.ZERO);
            resultado.add(new MesDTO(mesAnioClave, total));

            // Avanzar al siguiente mes
            fechaActual = fechaActual.plusMonths(1);
        }

        return resultado;
    }

    // Totales y cantidad de pedidos (SIN CAMBIOS)
    public BigDecimal totalPorDia(LocalDate dia) {
        return pedidosPorDia(dia).stream()
                .map(PedidoDiaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int cantidadPedidosPorDia(LocalDate dia) {
        return pedidosPorDia(dia).size();
    }

    public BigDecimal totalPorRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        return pedidoRepository.findByFechaBetween(inicioDateTime, finDateTime).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .map(Pedido::getTotalPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int cantidadPedidosPorRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        return (int) pedidoRepository.findByFechaBetween(inicioDateTime, finDateTime).stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .count();
    }
}