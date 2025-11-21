package com.zapastore.zapastore_h2.model.pedidos.dto;

import java.math.BigDecimal;

// 💡 Centralizamos todos los DTOs de métricas en un solo archivo
public class PedidosDTOs {

    // DTO para Ventas por Día
    public static class PedidoDiaDTO {
        private String hora;
        private BigDecimal total;

        public PedidoDiaDTO(String hora, BigDecimal total) {
            this.hora = hora;
            this.total = total;
        }

        public String getHora() { return hora; }
        public void setHora(String hora) { this.hora = hora; }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }

    // DTO para Ventas por Semana
    public static class DiaSemanaDTO {
        private String fecha;
        private BigDecimal total;

        public DiaSemanaDTO(String fecha, BigDecimal total) {
            this.fecha = fecha;
            this.total = total;
        }

        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }

    // DTO para Ventas por Mes
    public static class MesDTO {
        private String mes;
        private BigDecimal total;

        public MesDTO(String mes, BigDecimal total) {
            this.mes = mes;
            this.total = total;
        }

        public String getMes() { return mes; }
        public void setMes(String mes) { this.mes = mes; }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }
}
