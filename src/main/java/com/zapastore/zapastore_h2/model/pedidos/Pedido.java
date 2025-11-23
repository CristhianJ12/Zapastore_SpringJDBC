package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.detalle_pedido.DetallePedido;
import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

public class Pedido {

    private Integer id;
    private Usuario cliente;
    private BigDecimal totalPagar;
    private BigDecimal costoEnvio;
    private LocalDateTime fecha;
    private String estado;
    private List<DetallePedido> detalles;

    public Pedido() {}

    public Pedido(Integer id, Usuario cliente, BigDecimal totalPagar,
                  BigDecimal costoEnvio, LocalDateTime fecha, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.totalPagar = totalPagar;
        this.costoEnvio = costoEnvio;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Timestamp getFechaTimestamp() {
        if (this.fecha == null) {
            return null;
        }
        return Timestamp.valueOf(this.fecha);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}