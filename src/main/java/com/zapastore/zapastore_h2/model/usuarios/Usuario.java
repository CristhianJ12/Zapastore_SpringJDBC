package com.zapastore.zapastore_h2.model.usuarios;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private String rol;
    private String estado;
    private boolean activo;

    public Usuario() {
        this.rol = "CLIENTE";
        this.estado = "Activo";
        this.activo = true;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol != null ? rol.toUpperCase() : "CLIENTE";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        this.activo = "Activo".equalsIgnoreCase(estado);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        this.estado = activo ? "Activo" : "Inactivo";
    }
}