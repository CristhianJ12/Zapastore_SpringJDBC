package com.zapastore.zapastore_h2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapastore.zapastore_h2.model.pedidos.MetricaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
public class MetricasController {

    private final MetricaService metricaService;

    public MetricasController(MetricaService metricaService) {
        this.metricaService = metricaService;
    }

    @GetMapping("/admin/metricas")
    public String showMetricas(
            @RequestParam(value = "dia", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia,
            @RequestParam(value = "semana", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSemana,
            @RequestParam(value = "mesInicio", required = false) Integer mesInicio,
            @RequestParam(value = "mesFin", required = false) Integer mesFin,
            @RequestParam(value = "anio", required = false) Integer anio,
            Model model) throws JsonProcessingException {

        LocalDate diaSeleccionado = dia != null ? dia : LocalDate.now();
        LocalDate inicioSemana = fechaSemana != null ? fechaSemana.minusDays(6) : LocalDate.now().minusDays(6);
        LocalDate finSemana = fechaSemana != null ? fechaSemana : LocalDate.now();
        LocalDate inicioMes = (mesInicio != null && anio != null) ? LocalDate.of(anio, mesInicio, 1) : LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = (mesFin != null && anio != null) ? LocalDate.of(anio, mesFin, 1).with(TemporalAdjusters.lastDayOfMonth()) : LocalDate.now();

        ObjectMapper mapper = new ObjectMapper();

        // Pasar JSON al JSP
        model.addAttribute("pedidosDiaJson", mapper.writeValueAsString(metricaService.pedidosPorDia(diaSeleccionado)));
        model.addAttribute("diasSemanaJson", mapper.writeValueAsString(metricaService.ventasPorDiaEnRango(inicioSemana, finSemana)));
        model.addAttribute("mesesJson", mapper.writeValueAsString(metricaService.ventasPorMesEnRango(inicioMes, finMes)));

        // Totales
        model.addAttribute("totalDia", metricaService.totalPorDia(diaSeleccionado));
        model.addAttribute("pedidosDia", metricaService.cantidadPedidosPorDia(diaSeleccionado));
        model.addAttribute("totalSemana", metricaService.totalPorRango(inicioSemana, finSemana));
        model.addAttribute("pedidosSemana", metricaService.cantidadPedidosPorRango(inicioSemana, finSemana));
        model.addAttribute("totalMes", metricaService.totalPorRango(inicioMes, finMes));
        model.addAttribute("pedidosMes", metricaService.cantidadPedidosPorRango(inicioMes, finMes));

        // Filtros
        model.addAttribute("diaSeleccionado", diaSeleccionado);
        model.addAttribute("fechaSemana", finSemana);
        model.addAttribute("mesInicio", mesInicio != null ? mesInicio : inicioMes.getMonthValue());
        model.addAttribute("mesFin", mesFin != null ? mesFin : finMes.getMonthValue());
        model.addAttribute("anio", anio != null ? anio : inicioMes.getYear());

        return "admin/metricas";
    }
}
