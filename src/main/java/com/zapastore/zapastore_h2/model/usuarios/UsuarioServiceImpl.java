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
        // La lógica de verificación de credenciales y estado "Activo" se maneja en el DAO
        return usuarioDAO.findByCorreoAndContrasena(correo, contrasena);
    }

    // ======================================
    // Registro de Cliente
    // ======================================
    @Override
    public boolean registrarCliente(Usuario usuario, String confirmPassword) {
        // 1. Lógica de negocio: validación de contraseñas
        if (!usuario.getContrasena().equals(confirmPassword)) {
            // Podrías lanzar una excepción o retornar false
            return false; 
        }

        // 2. Lógica de negocio: verificación de existencia
        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            return false; // Correo ya existe
        }
        
        // 3. Lógica de negocio: asignar roles y estado por defecto (opcional, ya se hace en el DAO)
        usuario.setRol("cliente");
        usuario.setEstado("Activo");

        // 4. Persistencia
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
    public boolean guardarUsuario(Usuario usuario) {
        // En un caso real, aquí irían validaciones de formato, longitud, etc.
        // También la lógica para hashear la contraseña si se está creando un usuario
        if (usuarioDAO.existsByCorreo(usuario.getCorreo())) {
            // Podrías verificar que no exista solo si es una operación de CREACIÓN
            return false; 
        }
        return usuarioDAO.save(usuario);
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        // Lógica de negocio: asegurar que el ID exista antes de actualizar
        if (usuarioDAO.buscarPorId(usuario.getIdUsuario()).isEmpty()) {
            return false;
        }
        return usuarioDAO.actualizar(usuario);
    }

    @Override
    public boolean desactivarUsuario(String id) {
        // Lógica de negocio: verificar si es un admin el que desactiva, etc.
        return usuarioDAO.desactivar(id);
    }

    @Override
    public boolean activarUsuario(String id) {
        return usuarioDAO.activar(id);
    }

    @Override
    public boolean eliminarUsuario(String id) {
        // Lógica de negocio: chequear dependencias o permisos finales
        return usuarioDAO.eliminar(id);
    }
}