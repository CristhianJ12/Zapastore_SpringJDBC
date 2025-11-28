package com.zapastore.zapastore_h2.model.usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> login(String correo, String contrasena);

    boolean registrarCliente(Usuario usuario, String confirmPassword);

    List<Usuario> listarTodos();

    Optional<Usuario> buscarPorId(String id);

    Optional<Usuario> buscarPorCorreo(String correo);

    boolean guardarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    boolean desactivarUsuario(String id);

    boolean activarUsuario(String id);

    boolean eliminarUsuario(String id);
}