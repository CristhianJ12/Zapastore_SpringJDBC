package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {

    private Integer id;
    private Usuario cliente;
    private BigDecimal totalPagar;
    private BigDecimal costoEnvio;
    private LocalDateTime fecha;
    private String estado;

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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public BigDecimal getTotalPagar() { return totalPagar; }
    public void setTotalPagar(BigDecimal totalPagar) { this.totalPagar = totalPagar; }

    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(BigDecimal costoEnvio) { this.costoEnvio = costoEnvio; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
