package com.zapastore.zapastore_h2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapastore.zapastore_h2.model.pedidos.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
public class MetricasController {

    private final PedidoService pedidoService;

    public MetricasController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
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

        model.addAttribute("pedidosDiaJson", mapper.writeValueAsString(pedidoService.obtenerVentasPorDia(diaSeleccionado)));
        model.addAttribute("diasSemanaJson", mapper.writeValueAsString(pedidoService.obtenerVentasPorSemana(inicioSemana, finSemana)));
        model.addAttribute("mesesJson", mapper.writeValueAsString(pedidoService.obtenerVentasPorMes(inicioMes, finMes)));

        model.addAttribute("totalDia", pedidoService.calcularTotalVentasPorRango(diaSeleccionado, diaSeleccionado));
        model.addAttribute("pedidosDia", pedidoService.contarPedidosCompletadosPorRango(diaSeleccionado, diaSeleccionado));
        model.addAttribute("totalSemana", pedidoService.calcularTotalVentasPorRango(inicioSemana, finSemana));
        model.addAttribute("pedidosSemana", pedidoService.contarPedidosCompletadosPorRango(inicioSemana, finSemana));
        model.addAttribute("totalMes", pedidoService.calcularTotalVentasPorRango(inicioMes, finMes));
        model.addAttribute("pedidosMes", pedidoService.contarPedidosCompletadosPorRango(inicioMes, finMes));

        model.addAttribute("diaSeleccionado", diaSeleccionado);
        model.addAttribute("fechaSemana", finSemana);
        model.addAttribute("mesInicio", mesInicio != null ? mesInicio : inicioMes.getMonthValue());
        model.addAttribute("mesFin", mesFin != null ? mesFin : finMes.getMonthValue());
        model.addAttribute("anio", anio != null ? anio : inicioMes.getYear());

        return "admin/metricas";
    }
}