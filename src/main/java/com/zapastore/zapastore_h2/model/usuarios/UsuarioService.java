package com.zapastore.zapastore_h2.model.usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    
    // Autenticación
    Optional<Usuario> login(String correo, String contrasena);
    
    // Registro
    boolean registrarCliente(Usuario usuario, String confirmPassword);
    
    // CRUD para Administrador
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(String id);
    boolean guardarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    boolean desactivarUsuario(String id);
    boolean activarUsuario(String id);
    boolean eliminarUsuario(String id);
}