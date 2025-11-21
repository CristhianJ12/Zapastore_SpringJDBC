package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.categoria.Categoria;
import com.zapastore.zapastore_h2.model.categoria.CategoriaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // LISTAR CATEGORÍAS
    @GetMapping
    public String listar(Model model, @RequestParam(value = "msg", required = false) String msg) {
        List<Categoria> lista = categoriaService.listarCategorias();
        model.addAttribute("categorias", lista);
        model.addAttribute("msg", msg);
        return "admin/categoriaLista";
    }

    // MOSTRAR FORMULARIO EDITAR
    @GetMapping("/mostrarEditar")
    public String mostrarEditar(@RequestParam("id") Integer id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id);

        if (categoria == null) {
            return "redirect:/admin/categorias?msg=Categoría no encontrada";
        }

        model.addAttribute("categoria", categoria);
        return "admin/categoriaEditar";
    }

    // GUARDAR (NUEVA)
    @PostMapping("/guardar")
    public String guardar(@RequestParam("nombre") String nombre,
                          @RequestParam(value = "estado", defaultValue = "Activo") String estado) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "redirect:/admin/categorias?msg=El nombre no puede estar vacío";
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre.trim());
        categoria.setEstado(estado);

        categoriaService.guardar(categoria);

        return "redirect:/admin/categorias?msg=Categoría registrada correctamente";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam("id") Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("estado") String estado,
            Model model) {

        Categoria categoria = categoriaService.buscarPorId(id);

        if (categoria == null) {
            return "redirect:/admin/categorias?msg=Categoría no encontrada";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            model.addAttribute("categoria", categoria);
            model.addAttribute("msg", "El nombre no puede estar vacío");
            return "admin/categoriaEditar";
        }

        categoria.setNombre(nombre.trim());
        categoria.setEstado(estado);
        categoriaService.actualizar(categoria);

        return "redirect:/admin/categorias?msg=Categoría actualizada correctamente";
    }

    // DESACTIVAR (NO ELIMINAR)
    @GetMapping("/eliminar")
    public String desactivar(@RequestParam("id") Integer id) {
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
