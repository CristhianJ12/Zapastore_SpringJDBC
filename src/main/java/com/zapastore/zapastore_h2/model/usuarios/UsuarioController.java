package com.zapastore.zapastore_h2.model.usuarios; // Es una buena práctica poner los controladores en un paquete 'controller'

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional; // 💡 Import Faltante: Necesario para manejar Optional

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Inyección de dependencia por constructor (Recomendada por Spring)
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==========================================================
    // RUTAS GET: MOSTRAR LISTA Y FORMULARIOS
    // ==========================================================

    // 🔹 1. Mostrar lista de usuarios (GET /admin/usuarios)
    @GetMapping
    public String listarUsuarios(Model model,
                                 @ModelAttribute("msg") String msg,
                                 @ModelAttribute("error") String error) {
        
        // 💡 CORRECCIÓN: Usar el método listarTodos() del servicio
        List<Usuario> usuarios = usuarioService.listarTodos(); 
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("msg", msg);
        model.addAttribute("error", error);
        return "admin/usuarioLista";
    }

    // 🔹 2. Mostrar formulario para CREAR (GET /admin/usuarios/mostrarCrear)
    @GetMapping("/mostrarCrear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarioForm"; 
    }

    // 🔹 3. Mostrar formulario para EDITAR (GET /admin/usuarios/mostrarEditar/{id})
    @GetMapping("/mostrarEditar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        
        // 💡 CORRECCIÓN: Manejar Optional<Usuario>
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) { // Usar isEmpty() en Optional
            ra.addFlashAttribute("error", "Usuario no encontrado para edición.");
            return "redirect:/admin/usuarios";
        }
        
        model.addAttribute("usuario", usuarioOpt.get()); // Obtener el Usuario del Optional
        return "admin/usuarioForm"; 
    }

    // ==========================================================
    // RUTAS POST: GUARDAR Y ACTUALIZAR
    // ==========================================================
    
    // 🔹 4. Guardar nuevo usuario (POST /admin/usuarios/guardar)
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, // Usar @ModelAttribute explícitamente es buena práctica
                                 @RequestParam("confirmContrasena") String confirmContrasena, // Campo que falta en el modelo
                                 RedirectAttributes ra) {
        
        // 💡 Lógica de validación básica de campos no vacíos (Contrasena confirmación se manejará en el Service)
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty() || 
            usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            ra.addFlashAttribute("error", "El correo y la contraseña son obligatorios.");
            return "redirect:/admin/usuarios/mostrarCrear";
        }
        
        // 💡 CORRECCIÓN: Usar la lógica de negocio del servicio para guardar/registrar
        // Asumimos que para crear desde admin, solo se necesita el método `guardarUsuario`
        // Si quieres verificar la existencia por correo, usa el método del Service.
        
        // Verifica si el correo ya existe
        Optional<Usuario> usuarioExistente = usuarioService.buscarPorId(usuario.getCorreo());
        if (usuarioExistente.isPresent()) {
             ra.addFlashAttribute("error", "El correo ya está registrado.");
             return "redirect:/admin/usuarios/mostrarCrear";
        }

        // Si se va a crear un usuario, el service debe manejar el estado y rol por defecto
        boolean guardado = usuarioService.guardarUsuario(usuario);

        if (guardado) {
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " registrado correctamente.");
        } else {
             ra.addFlashAttribute("error", "Error al registrar el usuario.");
        }
        
        return "redirect:/admin/usuarios";
    }

    // 🔹 5. Actualizar usuario existente (POST /admin/usuarios/actualizar)
    @PostMapping("/actualizar")
    public String actualizarUsuario(Usuario usuario, 
                                     @RequestParam(value = "contrasenaNueva", required = false) String contrasenaNueva, // Usar un nombre distinto para la contraseña
                                     RedirectAttributes ra) {
                                            
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            ra.addFlashAttribute("error", "El nombre no puede estar vacío.");
            return "redirect:/admin/usuarios/mostrarEditar/" + usuario.getIdUsuario();
        }

        // 1. Obtener el usuario existente de la DB
        Optional<Usuario> usuarioExistenteOpt = usuarioService.buscarPorId(usuario.getIdUsuario());
        if (usuarioExistenteOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Error: Usuario no encontrado para actualizar.");
            return "redirect:/admin/usuarios";
        }
        Usuario usuarioExistente = usuarioExistenteOpt.get();
        
        // 2. Transferir los campos actualizables
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setEstado(usuario.getEstado()); 

        // 3. Si se proporciona una nueva contraseña, actualizarla en el objeto
        if (contrasenaNueva != null && !contrasenaNueva.trim().isEmpty()) {
            usuarioExistente.setContrasena(contrasenaNueva.trim());
        } else {
            // Aseguramos que la contraseña no se sobrescriba con vacío en el service/dao
            // Al no pasarla en el formulario y no ponerla en el objeto, el service la debe ignorar.
            // Para asegurar esto, seteamos la contraseña a null/vacío si no se envía una nueva.
            usuarioExistente.setContrasena(null); 
        }

        // 💡 CORRECCIÓN: Llamar al método actualizado del servicio
        usuarioService.actualizarUsuario(usuarioExistente); 

        ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " actualizado correctamente.");
        return "redirect:/admin/usuarios";
    }
    
    // ==========================================================
    // RUTAS GET: GESTIÓN DE ESTADO (Eliminación Lógica)
    // ==========================================================

    // 🔹 6. Desactivar usuario (GET /admin/usuarios/desactivar/{id})
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {
        
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
        Usuario usuario = usuarioOpt.get();

        // 💡 CORRECCIÓN: Usar el método del servicio y verificar si la operación fue exitosa
        if (usuario.isActivo() && usuarioService.desactivarUsuario(id)) { 
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " desactivado correctamente.");
        } else {
            ra.addFlashAttribute("error", "El usuario ya estaba inactivo o hubo un error.");
        }
        
        return "redirect:/admin/usuarios";
    }
    
    // 🔹 7. Activar usuario (GET /admin/usuarios/activar/{id})
    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {
        
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
        Usuario usuario = usuarioOpt.get();
        
        // 💡 CORRECCIÓN: Usar el método del servicio y verificar si la operación fue exitosa
        if (!usuario.isActivo() && usuarioService.activarUsuario(id)) { 
            ra.addFlashAttribute("msg", "Usuario " + usuario.getNombre() + " activado correctamente.");
        } else {
            ra.addFlashAttribute("error", "El usuario ya estaba activo o hubo un error.");
        }
        
        return "redirect:/admin/usuarios";
    }

    // 🔹 8. Eliminar usuario (GET /admin/usuarios/eliminar/{id})
    // Se recomienda usar DELETE mapping, pero se usa GET por simplicidad en navegadores/formularios simples.
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") String id, RedirectAttributes ra) {
        // Podrías buscar el usuario primero para obtener el nombre
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