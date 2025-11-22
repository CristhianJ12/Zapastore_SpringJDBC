package com.zapastore.zapastore_h2.model.detalle_pedido;

import com.zapastore.zapastore_h2.model.producto.Producto;
import java.math.BigDecimal;

public class DetallePedido {

    private Integer id;
    private Integer pedidoId;
    private Integer productoId;
    private Integer cantidad;
    private Integer talla;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String nombreProducto;

    // OBJETO COMPLETO PARA JSP (no mapeado por el RowMapper)
    private Producto producto;

    public DetallePedido() {}

    // Constructor completo (usado para inserción o representación)
    public DetallePedido(Integer id, Integer pedidoId, Integer productoId,
                         Integer cantidad, Integer talla,
                         BigDecimal precioUnitario, BigDecimal subtotal,
                         String nombreProducto) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.talla = talla;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.nombreProducto = nombreProducto;
    }

    public void calcularSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // GETTERS Y SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Integer getTalla() { return talla; }
    public void setTalla(Integer talla) { this.talla = talla; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}