package com.zapastore.zapastore_h2.model.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    // Inyectamos el DAO/Repository
    @Autowired
    private UsuarioDAO usuarioDAO;

    // ======================================
    // Autenticación
    // ======================================
    @Override
    public Optional<Usuario> login(String correo, String contrasena) {

        // 1) Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioDAO.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) return Optional.empty();

        Usuario usuario = usuarioOpt.get();

        // 2) Comparación directa (SIN BCrypt)
        if (usuario.getContrasena() == null) return Optional.empty();

        boolean matches = usuario.getContrasena().equals(contrasena);

        if (matches && usuario.isActivo()) {
            // Opcional: no exponemos contraseña
            usuario.setContrasena(null);
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    // ======================================
    // Registro de Cliente
    // ======================================
    @Override
    public boolean registrarCliente(Usuario usuario, String confirmPassword) {

        // Validación básica (sin hashing)
        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(confirmPassword)) {
            return false;
        }

        // Verificar existencia por correo
        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        // Asignar rol/estado por defecto si faltan
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("cliente");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("Activo");
        }

        // Se guarda tal cual, SIN encriptación
        return usuarioDAO.save(usuario);
    }

    // ======================================
    // CRUD para Administrador
    // ======================================

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

        // Evitar duplicación por correo
        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        // No se encripta la contraseña
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            // si admin no pone contraseña, la dejamos nula o generamos una por defecto
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

        // Verificar que exista
        if (usuarioDAO.buscarPorId(usuario.getIdUsuario()).isEmpty()) {
            return false;
        }

        // SIN hashing: si la contraseña viene vacía, el DAO no la debe modificar
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
