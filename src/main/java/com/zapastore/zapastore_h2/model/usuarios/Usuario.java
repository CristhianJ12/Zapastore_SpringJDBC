package com.zapastore.zapastore_h2.model.usuarios;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String correo;
    private String contrasena; // Solo para guardar/comparar, no se expone
    private String telefono;
    private String rol; // "admin" o "cliente"
    private String estado; // "Activo" o "Inactivo"
    private boolean activo; // Campo calculado basado en estado

    // Constructor (opcionalmente para pruebas/registro)
    public Usuario() {
        this.rol = "cliente"; // Rol por defecto al registrar
        this.estado = "Activo";
        this.activo = true;
    }

    // Getters y Setters
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; this.activo = "Activo".equalsIgnoreCase(estado); }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; this.estado = activo ? "Activo" : "Inactivo"; }
}