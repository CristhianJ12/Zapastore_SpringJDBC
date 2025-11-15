package com.zapastore.zapastore_h2.model.usuarios;

import java.util.List;
import java.util.Optional;

// 💡 Interfaz UsuarioDAO
public interface UsuarioDAO {
    
    // 💡 CORRECCIÓN: El tipo de retorno debe ser Optional<Usuario>
    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);
    
    boolean existsByCorreo(String correo);
    
    List<Usuario> listarUsuarios(); // Este método también podría causar un error si no coincide con el Repository.
    
    Optional<Usuario> buscarPorId(String id);
    
    boolean save(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean desactivar(String id);
    boolean activar(String id);
    boolean eliminar(String id);
}