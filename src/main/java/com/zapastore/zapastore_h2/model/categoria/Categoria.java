package com.zapastore.zapastore_h2.model.categoria;

public class Categoria {

    private Integer id;
    private String nombre;
    private String estado; // "Activo" / "Inactivo"

    public Categoria() {
    }

    public Categoria(Integer id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
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
