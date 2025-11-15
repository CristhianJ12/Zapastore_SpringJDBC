package com.zapastore.zapastore_h2.model.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository 
public class UsuarioRepository implements UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Mapeador de filas corregido. Usa los nombres de columna EXACTOS
     * de la base de datos: IDUsuario y Rol (con mayúsculas).
     */
    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        // 💡 CORRECCIÓN: Usar IDUsuario (mayúsculas) según el script SQL
        usuario.setIdUsuario(rs.getString("IDUsuario")); 
        usuario.setNombre(rs.getString("nombre"));
        usuario.setCorreo(rs.getString("correo"));
        // NOTA: No se lee la contraseña aquí por seguridad
        usuario.setTelefono(rs.getString("telefono"));
        // 💡 CORRECCIÓN: Usar Rol (con mayúscula) según el script SQL
        usuario.setRol(rs.getString("Rol")); 
        usuario.setEstado(rs.getString("estado"));
        return usuario;
    };

    // -----------------------------------------------------
    // 1. Implementación de Autenticación y Registro
    // -----------------------------------------------------

    @Override
    public Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena) {
        // La consulta SQL está bien
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        try {
            // queryForObject espera exactamente un resultado. Si no lo encuentra, lanza EmptyResultDataAccessException.
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, correo, contrasena);
            
            // Si llega aquí, el usuario existe. Verificamos el estado.
            if (usuario != null && usuario.isActivo()) {
                return Optional.of(usuario);
            }
            return Optional.empty(); 
        } catch (EmptyResultDataAccessException e) {
            // No se encontró el usuario
            return Optional.empty(); 
        }
    }

    @Override
    public boolean existsByCorreo(String correo) {
        String sql = "SELECT count(*) FROM usuarios WHERE correo = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, correo);
        return count != null && count > 0;
    }

    // -----------------------------------------------------
    // 2. Implementación de Métodos CRUD
    // -----------------------------------------------------

    @Override
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }
    
    @Override
    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE IDUsuario = ?"; // 💡 CORRECCIÓN: Usar IDUsuario en la cláusula WHERE
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, id);
            return Optional.ofNullable(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean save(Usuario usuario) {
        // 💡 CORRECCIÓN: Usar IDUsuario y Rol en la lista de columnas
        String sql = "INSERT INTO usuarios (IDUsuario, nombre, correo, contrasena, telefono, Rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            usuario.setIdUsuario(UUID.randomUUID().toString());
        }
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("cliente");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("Activo");
        }

        int rows = jdbcTemplate.update(sql, 
            usuario.getIdUsuario(), 
            usuario.getNombre(), 
            usuario.getCorreo(), 
            usuario.getContrasena(),
            usuario.getTelefono(),
            usuario.getRol(), // El valor de Java aquí está bien
            usuario.getEstado()
        );
        return rows > 0;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        // 💡 CORRECCIÓN: Usar Rol en la sentencia UPDATE y IDUsuario en el WHERE
        String baseSql = "UPDATE usuarios SET nombre = ?, telefono = ?, Rol = ?, estado = ?";
        
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            baseSql += ", contrasena = ?";
            baseSql += " WHERE IDUsuario = ?";
            
            int rows = jdbcTemplate.update(baseSql, 
                usuario.getNombre(), 
                usuario.getTelefono(), 
                usuario.getRol(), 
                usuario.getEstado(),
                usuario.getContrasena(), 
                usuario.getIdUsuario()
            );
            return rows > 0;
        } else {
            baseSql += " WHERE IDUsuario = ?";
            
            int rows = jdbcTemplate.update(baseSql, 
                usuario.getNombre(), 
                usuario.getTelefono(), 
                usuario.getRol(), 
                usuario.getEstado(),
                usuario.getIdUsuario()
            );
            return rows > 0;
        }
    }

    @Override
    public boolean desactivar(String id) {
        String sql = "UPDATE usuarios SET estado = 'Inactivo' WHERE IDUsuario = ?"; // 💡 CORRECCIÓN: IDUsuario
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }

    @Override
    public boolean activar(String id) {
        String sql = "UPDATE usuarios SET estado = 'Activo' WHERE IDUsuario = ?"; // 💡 CORRECCIÓN: IDUsuario
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }
    
    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM usuarios WHERE IDUsuario = ?"; // 💡 CORRECCIÓN: IDUsuario
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }
}