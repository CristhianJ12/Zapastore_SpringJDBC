package com.zapastore.zapastore_h2.model.producto;

import java.math.BigDecimal;
import java.util.List;

public class Producto {

    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private String imagenUrl;
    private String descripcion;
    private String estado;
    private Integer categoriaID;
    private String categoriaNombre;
    private List<Integer> tallas;

    public Producto() {
    }

    public Producto(Integer id, String nombre, BigDecimal precio, String imagenUrl, String descripcion,
                    String estado, Integer categoriaID, String categoriaNombre, List<Integer> tallas) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoriaID = categoriaID;
        this.categoriaNombre = categoriaNombre;
        this.tallas = tallas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Integer categoriaID) {
        this.categoriaID = categoriaID;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public List<Integer> getTallas() {
        return tallas;
    }

    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }
}