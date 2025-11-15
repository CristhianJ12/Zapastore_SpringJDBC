package com.zapastore.zapastore_h2.model.categoria;

public class Categoria {
    private int categoriaId;
    private String nombre;
    private String estado; // "Activo" o "Inactivo"

    public Categoria() {
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return "Activo".equalsIgnoreCase(estado);
    }
}
