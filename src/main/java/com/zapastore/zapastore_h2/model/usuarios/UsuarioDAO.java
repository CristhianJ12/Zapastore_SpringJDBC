package com.zapastore.zapastore_h2.model.usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorId(String id);

    boolean save(Usuario usuario);

    boolean actualizar(Usuario usuario);

    boolean desactivar(String id);

    boolean activar(String id);

    boolean eliminar(String id);
}