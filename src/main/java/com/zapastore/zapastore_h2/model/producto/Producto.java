package com.zapastore.zapastore_h2.model.producto;

public class Producto {

    private Integer id;
    private String nombre;
    private double precio;
    private String imagenUrl;
    private String descripcion;
    private String estado;
    private Integer categoriaID; // CORRECCIÓN: CAMBIADO de 'int' a 'Integer'
    private String categoriaNombre; // campo transitorio (JOIN con categoría)

    public Producto() {
    }

    public Producto(Integer id, String nombre, double precio, String imagenUrl, String descripcion,
                    String estado, Integer categoriaID, String categoriaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoriaID = categoriaID;
        this.categoriaNombre = categoriaNombre;
    }

    // Getters y setters
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
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

    // CORRECCIÓN en Getter y Setter
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
}