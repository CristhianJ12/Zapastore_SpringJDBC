package com.zapastore.zapastore_h2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.model.usuarios.UsuarioDAO;

@Controller
public class AuthController {

    @Autowired
    private UsuarioDAO usuarioDao;

    // ===============================
    // LOGIN
    // ===============================
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login"; // Asegúrate que tu JSP sea login.jsp
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,
                        @RequestParam("contrasena") String contrasena,
                        HttpSession session, Model model) {

        Usuario usuario = usuarioDao.findByCorreo(correo).orElse(null);

        if (usuario != null && usuario.getContrasena() != null &&
                usuario.getContrasena().equals(contrasena)) {

            // 💡 CRÍTICO: El objeto 'usuario' que viene del findByCorreo ya tiene el rol en mayúsculas
            // gracias al RowMapper actualizado en UsuarioRepository.java.

            session.setAttribute("usuarioSesion", usuario);

            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/admin/metricas";
            } else if ("CLIENTE".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/cliente/home";
            }
        }

        model.addAttribute("error", "Credenciales incorrectas o usuario inactivo.");
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // ===============================
    // LOGOUT
    // ===============================
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("msg", "Sesión cerrada correctamente.");
        return "redirect:/login";
    }

    // ===============================
    // REGISTRO
    // ===============================
    @GetMapping("/registrar")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar"; // Asegúrate que tu JSP sea registrar.jsp
    }

    @PostMapping("/registro")
    public String register(@ModelAttribute("usuario") Usuario usuario,
                           @RequestParam("confirmarContrasena") String confirmPassword,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (!usuario.getContrasena().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "registrar";
        }

        if (usuarioDao.existsByCorreo(usuario.getCorreo())) {
            model.addAttribute("error", "El correo electrónico ya está registrado.");
            return "registrar";
        }

        // Por defecto, rol CLIENTE y Activo (la entidad Usuario y el Repository se encargarán de ponerlo en mayúsculas)
        usuario.setRol("CLIENTE");
        usuario.setEstado("Activo");

        usuarioDao.save(usuario);

        redirectAttributes.addFlashAttribute("msg", "Registro exitoso. Por favor, inicia sesión.");
        return "redirect:/login";
    }
}