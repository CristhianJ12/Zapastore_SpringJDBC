package com.zapastore.zapastore_h2.model.usuarios;

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

    // ==========================================================
    // LISTAR
    // ==========================================================
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

    // ==========================================================
    // FORMULARIOS
    // ==========================================================
    @GetMapping("/mostrarCrear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarioForm";
    }

    @GetMapping("/mostrarEditar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") String id, Model model, RedirectAttributes ra) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado para edición.");
            return "redirect:/admin/usuarios";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        return "admin/usuarioForm";
    }

    // ==========================================================
    // GUARDAR NUEVO USUARIO
    // ==========================================================
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
                                 @RequestParam("confirmContrasena") String confirmContrasena,
                                 RedirectAttributes ra) {

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty() ||
                usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {

            ra.addFlashAttribute("error", "El correo y la contraseña son obligatorios.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }

        // *** CORRECCIÓN: validar correo EXISTENTE correctamente ***
        if (usuarioService.listarTodos().stream()
                .anyMatch(u -> u.getCorreo().equalsIgnoreCase(usuario.getCorreo()))) {

            ra.addFlashAttribute("error", "El correo ya está registrado.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }

        // *** CONFIRMACIÓN DE CONTRASEÑA (sin hashing) ***
        if (!usuario.getContrasena().equals(confirmContrasena)) {
            ra.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }

        boolean guardado = usuarioService.guardarUsuario(usuario);

        if (guardado) {
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " registrado correctamente.");
        } else {
            ra.addFlashAttribute("error", "Error al registrar el usuario.");
        }

        return "redirect:/admin/usuarios";
    }

    // ==========================================================
    // ACTUALIZAR
    // ==========================================================
    @PostMapping("/actualizar")
    public String actualizarUsuario(Usuario usuario,
                                    @RequestParam(value = "contrasenaNueva", required = false) String contrasenaNueva,
                                    RedirectAttributes ra) {

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            ra.addFlashAttribute("error", "El nombre no puede estar vacío.");
            return "redirect:/admin/usuarios/mostrarEditar/" + usuario.getIdUsuario();
        }

        Optional<Usuario> usuarioExistenteOpt = usuarioService.buscarPorId(usuario.getIdUsuario());
        if (usuarioExistenteOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Error: Usuario no encontrado para actualizar.");
            return "redirect:/admin/usuarios";
        }

        Usuario usuarioExistente = usuarioExistenteOpt.get();

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setEstado(usuario.getEstado());

        if (contrasenaNueva != null && !contrasenaNueva.trim().isEmpty()) {
            usuarioExistente.setContrasena(contrasenaNueva.trim());
        } else {
            usuarioExistente.setContrasena(null);
        }

        usuarioService.actualizarUsuario(usuarioExistente);

        ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " actualizado correctamente.");
        return "redirect:/admin/usuarios";
    }

    // ==========================================================
    // ACTIVAR / DESACTIVAR / ELIMINAR
    // ==========================================================
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.isActivo() && usuarioService.desactivarUsuario(id)) {
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " desactivado correctamente.");
        } else {
            ra.addFlashAttribute("error", "El usuario ya estaba inactivo o hubo un error.");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isActivo() && usuarioService.activarUsuario(id)) {
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " activado correctamente.");
        } else {
            ra.addFlashAttribute("error", "El usuario ya estaba activo o hubo un error.");
        }

        return "redirect:/admin/usuarios";
    }

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
