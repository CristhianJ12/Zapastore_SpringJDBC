package com.zapastore.zapastore_h2.model.usuarios;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Optional<Usuario> login(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getContrasena() == null) {
            return Optional.empty();
        }

        boolean matches = usuario.getContrasena().equals(contrasena);

        if (matches && usuario.isActivo()) {
            usuario.setContrasena(null);
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    @Override
    public boolean registrarCliente(Usuario usuario, String confirmPassword) {
        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(confirmPassword)) {
            return false;
        }

        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("cliente");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("Activo");
        }

        return usuarioDAO.save(usuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarUsuarios();
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioDAO.buscarPorId(id);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioDAO.findByCorreo(correo);
    }

    @Override
    public boolean guardarUsuario(Usuario usuario) {
        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(usuario.getContrasena());
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("cliente");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("Activo");
        }

        return usuarioDAO.save(usuario);
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuarioDAO.buscarPorId(usuario.getIdUsuario()).isEmpty()) {
            return false;
        }

        if (usuario.getContrasena() != null && usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(null);
        }

        return usuarioDAO.actualizar(usuario);
    }

    @Override
    public boolean desactivarUsuario(String id) {
        return usuarioDAO.desactivar(id);
    }

    @Override
    public boolean activarUsuario(String id) {
        return usuarioDAO.activar(id);
    }

    @Override
    public boolean eliminarUsuario(String id) {
        return usuarioDAO.eliminar(id);
    }
}