package com.zapastore.zapastore_h2.model.detalle_pedido;

import com.zapastore.zapastore_h2.model.producto.Producto;
import java.math.BigDecimal;

public class ItemCarrito {

    private Integer id;
    private Integer detalleId;
    private Producto producto;
    private Integer cantidad;
    private Integer talla;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private boolean productoInactivo; // NUEVO CAMPO

    public ItemCarrito() {}

    public ItemCarrito(Integer id, Producto producto, Integer cantidad, Integer talla) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.talla = talla;
        this.precioUnitario = producto.getPrecio();
        this.productoInactivo = false; // Por defecto activo
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // Getters y Setters existentes
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(Integer detalleId) {
        this.detalleId = detalleId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Integer getTalla() {
        return talla;
    }

    public void setTalla(Integer talla) {
        this.talla = talla;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // NUEVO: Getter y Setter para productoInactivo
    public boolean isProductoInactivo() {
        return productoInactivo;
    }

    public void setProductoInactivo(boolean productoInactivo) {
        this.productoInactivo = productoInactivo;
    }
}