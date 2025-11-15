package com.zapastore.zapastore_h2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.model.usuarios.UsuarioDAO;

@Controller
public class AuthController {

    @Autowired
    private UsuarioDAO usuarioDao;

    // ======================================
    // Muestra formulario de LOGIN
    // ======================================
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "admin/login"; // Nombre del JSP: /WEB-INF/jsp/login.jsp
    }

    // ======================================
    // Procesa el LOGIN
    // ======================================
    @PostMapping("/login")
    public String login(String correo, String contrasena, HttpSession session, Model model) {
        Usuario usuario = usuarioDao.findByCorreoAndContrasena(correo, contrasena).orElse(null);

        if (usuario != null) {
            // Usuario autenticado y activo. Guardar en sesión
            session.setAttribute("usuarioSesion", usuario);

            if ("admin".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/admin/productos"; // Redirigir al panel de administrador
            } else if ("cliente".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/cliente/perfil"; // Redirigir al panel de cliente
            }
        }

        // Si falla la autenticación o está inactivo
        model.addAttribute("error", "Credenciales incorrectas o usuario inactivo.");
        model.addAttribute("usuario", new Usuario()); // Para mantener el correo en el formulario (si lo deseas)
        return "admin/login";
    }

    // ======================================
    // Cierre de sesión
    // ======================================
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addAttribute("logout", "success");
        return "redirect:/login";
    }
    
    // ======================================
    // Muestra formulario de REGISTRO
    // ======================================
    @GetMapping("/registrar")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/registrar"; // Nombre del JSP: /WEB-INF/jsp/registrar.jsp
    }

    // ======================================
    // Procesa el REGISTRO (Cliente)
    // ======================================
    @PostMapping("/register")
    public String register(@ModelAttribute("usuario") Usuario usuario, String confirmPassword, 
                           RedirectAttributes redirectAttributes, Model model) {

        if (!usuario.getContrasena().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "admin/registrar";
        }

        if (usuarioDao.existsByCorreo(usuario.getCorreo())) {
             model.addAttribute("error", "El correo electrónico ya está registrado.");
            return "admin/registrar";
        }

        // El rol se establece como "cliente" y estado "Activo" dentro del DAO/modelo
        usuarioDao.save(usuario);
        
        redirectAttributes.addFlashAttribute("msg", "Registro exitoso. Por favor, inicia sesión.");
        return "redirect:/login";
    }
}