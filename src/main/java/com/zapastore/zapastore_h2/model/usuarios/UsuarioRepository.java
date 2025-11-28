package com.zapastore.zapastore_h2.model.usuarios;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepository implements UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getString("IDUsuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setRol(rs.getString("Rol").toUpperCase());
        usuario.setEstado(rs.getString("estado"));
        return usuario;
    };

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, correo);
            return Optional.ofNullable(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByCorreo(String correo) {
        String sql = "SELECT count(*) FROM usuarios WHERE correo = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, correo);
        return count != null && count > 0;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE IDUsuario = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, id);
            return Optional.ofNullable(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (IDUsuario, nombre, correo, contrasena, telefono, Rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            usuario.setIdUsuario(UUID.randomUUID().toString());
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty() || "cliente".equalsIgnoreCase(usuario.getRol())) {
            usuario.setRol("CLIENTE");
        } else if ("admin".equalsIgnoreCase(usuario.getRol())) {
            usuario.setRol("ADMIN");
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
                usuario.getRol(),
                usuario.getEstado()
        );
        return rows > 0;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        if (usuario.getRol() != null) {
            usuario.setRol(usuario.getRol().toUpperCase());
        }

        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            String sql = "UPDATE usuarios SET nombre = ?, telefono = ?, Rol = ?, estado = ?, contrasena = ? WHERE IDUsuario = ?";
            int rows = jdbcTemplate.update(sql,
                    usuario.getNombre(),
                    usuario.getTelefono(),
                    usuario.getRol(),
                    usuario.getEstado(),
                    usuario.getContrasena(),
                    usuario.getIdUsuario()
            );
            return rows > 0;
        } else {
            String sql = "UPDATE usuarios SET nombre = ?, telefono = ?, Rol = ?, estado = ? WHERE IDUsuario = ?";
            int rows = jdbcTemplate.update(sql,
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
        String sql = "UPDATE usuarios SET estado = 'Inactivo' WHERE IDUsuario = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }

    @Override
    public boolean activar(String id) {
        String sql = "UPDATE usuarios SET estado = 'Activo' WHERE IDUsuario = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM usuarios WHERE IDUsuario = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }
}