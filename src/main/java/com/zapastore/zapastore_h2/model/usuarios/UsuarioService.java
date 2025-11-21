package com.zapastore.zapastore_h2.model.usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Autenticación
    Optional<Usuario> login(String correo, String contrasena);

    // Registro (método que valida confirmPassword y hashea la contraseña)
    boolean registrarCliente(Usuario usuario, String confirmPassword);

    // CRUD para Administrador
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(String id);
    Optional<Usuario> buscarPorCorreo(String correo); // nuevo método expuesto al service
    boolean guardarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    boolean desactivarUsuario(String id);
    boolean activarUsuario(String id);
    boolean eliminarUsuario(String id);
}
