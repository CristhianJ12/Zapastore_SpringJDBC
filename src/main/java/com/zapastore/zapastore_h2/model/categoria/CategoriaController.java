package com.zapastore.zapastore_h2.model.categoria; // Asumo que está en el paquete 'controller'

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 🔹 Mostrar lista de categorías
    @GetMapping("/admin/categorias")
    public String listarCategorias(Model model,
                                   @RequestParam(value = "msg", required = false) String msg) {
        List<Categoria> categorias = categoriaService.listarCategorias();
        model.addAttribute("categorias", categorias);
        model.addAttribute("msg", msg);
        return "admin/categoriaLista";
    }
    
    // 🔹 MOSTRAR FORMULARIO PARA EDITAR (Nueva Ruta GET)
    @GetMapping("/admin/categorias/mostrarEditar")
    public String mostrarFormularioEditar(@RequestParam("id") Integer id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id);
        
        // Verifica si la categoría fue encontrada
        if (categoria == null) {
            return "redirect:/admin/categorias?msg=Categoría no encontrada para editar";
        }
        
        model.addAttribute("categoria", categoria);
        return "admin/categoriaEditar"; 
    }

    // 🔹 Registrar nueva categoría
    @PostMapping("/admin/categorias/guardar")
    public String guardarCategoria(@RequestParam("nombre") String nombre,
                                   @RequestParam(value = "estado", required = false, defaultValue = "Activo") String estado) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "redirect:/admin/categorias?msg=El nombre no puede estar vacío";
        }

        // Verifica si ya existe (lógica simplificada)
        List<Categoria> existentes = categoriaService.listarCategorias();
        boolean existe = existentes.stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre.trim()));
        if (existe) {
            return "redirect:/admin/categorias?msg=La categoría ya existe";
        }

        Categoria c = new Categoria();
        c.setNombre(nombre.trim());
        c.setEstado(estado);
        categoriaService.guardar(c);

        return "redirect:/admin/categorias?msg=Categoría registrada correctamente";
    }

    // 🔹 ACTUALIZAR nombre o estado (Ruta POST que recibe los datos del formulario de edición)
    @PostMapping("/admin/categorias/actualizar") // RUTA CORREGIDA
    public String actualizarCategoria(
        @RequestParam("categoriaId") Integer id,
        @RequestParam("nombre") String nombre,
        @RequestParam("estado") String estado,
        Model model) {
        
        if (nombre == null || nombre.trim().isEmpty()) {
            // Si hay un error, recarga la vista de edición para mostrar el mensaje
            Categoria cError = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", cError);
            model.addAttribute("msg", "El nombre no puede estar vacío.");
            return "admin/categoriaEditar";
        }

        Categoria c = categoriaService.buscarPorId(id);
        if (c == null) {
            return "redirect:/admin/categorias?msg=Error: Categoría no encontrada para actualizar";
        }
        
        c.setNombre(nombre.trim());
        c.setEstado(estado);
        categoriaService.actualizar(c);

        return "redirect:/admin/categorias?msg=Categoría actualizada correctamente";
    }

    // 🔹 Cambiar estado a “Inactivo” (no se elimina)
    @GetMapping("/admin/categorias/eliminar")
    public String desactivarCategoria(@RequestParam("id") Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) {
            return "redirect:/admin/categorias?msg=Categoría no encontrada";
        }

        if ("Inactivo".equalsIgnoreCase(categoria.getEstado())) {
            return "redirect:/admin/categorias?msg=La categoría ya está inactiva";
        }

        categoria.setEstado("Inactivo");
        categoriaService.actualizar(categoria);

        return "redirect:/admin/categorias?msg=Categoría desactivada correctamente";
    }
}