package com.zapastore.zapastore_h2.model.usuarios;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ===========================
    // LISTAR USUARIOS
    // ===========================
    @GetMapping
    public String listarUsuarios(Model model,
                                 @ModelAttribute("msg") String msg,
                                 @ModelAttribute("error") String error) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("msg", msg);
        model.addAttribute("error", error);
        return "admin/usuarioLista";
    }

    // ===========================
    // FORMULARIO CREAR
    // ===========================
    @GetMapping("/mostrarCrear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarioForm";
    }

    // ===========================
    // FORMULARIO EDITAR
    // ===========================
    @GetMapping("/mostrarEditar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado para edición.");
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/usuarioForm";
    }

    // ===========================
    // GUARDAR (CREAR)
    // ===========================
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
                                 @RequestParam("confirmContrasena") String confirmContrasena,
                                 RedirectAttributes ra) {

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty() ||
                usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Correo y contraseña son obligatorios.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }

        if (!usuario.getContrasena().equals(confirmContrasena)) {
            ra.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }

        boolean guardado = usuarioService.guardarUsuario(usuario);
        if (guardado)
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " registrado correctamente.");
        else
            ra.addFlashAttribute("error", "Error al registrar el usuario.");

        return "redirect:/admin/usuarios";
    }

    // ===========================
    // ACTUALIZAR (EDITAR)
    // ===========================
    @PostMapping("/actualizar")
    public String actualizarUsuario(Usuario usuario,
                                    @RequestParam(value = "contrasena", required = false) String contrasena,
                                    RedirectAttributes ra,
                                    HttpSession session) {

        Optional<Usuario> usuarioExistenteOpt = usuarioService.buscarPorId(usuario.getIdUsuario());
        if (usuarioExistenteOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        Usuario usuarioExistente = usuarioExistenteOpt.get();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

        // 🚫 PROTECCIÓN: evitar autodesactivación vía edición
        if (usuarioSesion != null &&
                usuarioSesion.getIdUsuario().equals(usuario.getIdUsuario()) &&
                "Inactivo".equalsIgnoreCase(usuario.getEstado())) {

            ra.addFlashAttribute("error", "No puedes desactivar tu propia cuenta.");
            return "redirect:/admin/usuarios/mostrarEditar/" + usuario.getIdUsuario();
        }

        // CAMPOS EDITABLES
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setEstado(usuario.getEstado());

        // 🚫 NO PERMITIR CAMBIAR EL ROL EN EDICIÓN
        // usuarioExistente.setRol(usuario.getRol()); <-- NO SE USA. ROL FIJO.

        // Cambiar contraseña si viene una nueva
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuarioExistente.setContrasena(contrasena.trim());
        }

        usuarioService.actualizarUsuario(usuarioExistente);
        ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " actualizado correctamente.");

        return "redirect:/admin/usuarios";
    }

    // ===========================
    // DESACTIVAR
    // ===========================
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable("id") String id,
                                    RedirectAttributes ra,
                                    HttpSession session) {

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

        // 🚫 No se puede desactivar a sí mismo
        if (usuarioSesion != null && usuarioSesion.getIdUsuario().equals(id)) {
            ra.addFlashAttribute("error", "No puedes desactivar tu propia cuenta.");
            return "redirect:/admin/usuarios";
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        Usuario usuario = usuarioOpt.get();

        if (usuarioService.desactivarUsuario(id)) {
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " desactivado correctamente.");
        } else {
            ra.addFlashAttribute("error", "No se pudo desactivar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    // ===========================
    // ACTIVAR
    // ===========================
    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        if (usuarioService.activarUsuario(id)) {
            ra.addFlashAttribute("msg", "Usuario activado correctamente.");
        } else {
            ra.addFlashAttribute("error", "No se pudo activar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    // ===========================
    // ELIMINAR
    // ===========================
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        String nombreUsuario = usuarioOpt.map(Usuario::getNombre).orElse("desconocido");

        if (usuarioService.eliminarUsuario(id)) {
            ra.addFlashAttribute("msg", "Usuario " + nombreUsuario + " eliminado permanentemente.");
        } else {
            ra.addFlashAttribute("error", "Error al eliminar al usuario " + nombreUsuario + ".");
        }
        return "redirect:/admin/usuarios";
    }
}
