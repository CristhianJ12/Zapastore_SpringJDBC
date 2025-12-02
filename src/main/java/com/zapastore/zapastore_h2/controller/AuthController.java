package com.zapastore.zapastore_h2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.model.usuarios.UsuarioService;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,
                        @RequestParam("contrasena") String contrasena,
                        HttpSession session, Model model) {

        Usuario usuario = usuarioService.login(correo, contrasena).orElse(null);

        if (usuario != null) {
            session.setAttribute("usuarioSesion", usuario);
            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) return "redirect:/admin/metricas";
            else return "redirect:/cliente/home";
        }

        model.addAttribute("error", "Credenciales incorrectas o cuenta inactiva.");
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("msg", "Sesión cerrada correctamente.");
        return "redirect:/login";
    }

    @GetMapping("/registrar")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    @PostMapping("/registro")
    public String register(@ModelAttribute("usuario") Usuario usuario,
                           @RequestParam("confirmarContrasena") String confirmPassword,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        boolean registrado = usuarioService.registrarCliente(usuario, confirmPassword);
        if (!registrado) {
            model.addAttribute("error", "Error en registro: correo ya registrado o contraseñas no coinciden.");
            return "registrar";
        }

        redirectAttributes.addFlashAttribute("msg", "Registro exitoso. Por favor, inicia sesión.");
        return "redirect:/login";
    }
}
