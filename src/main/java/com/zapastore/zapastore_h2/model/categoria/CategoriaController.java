package com.zapastore.zapastore_h2.model.categoria;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 🔹 Mostrar lista
    @GetMapping("/admin/categorias")
    public String listarCategorias(Model model,
                                   @RequestParam(value = "msg", required = false) String msg) {
        List<Categoria> categorias = categoriaService.listarCategorias();
        model.addAttribute("categorias", categorias);
        model.addAttribute("msg", msg);
        return "admin/categoriaLista";
    }

    // 🔹 Registrar nueva categoría
    @PostMapping("/admin/categorias/guardar")
    public String guardarCategoria(@RequestParam("nombre") String nombre,
                                   @RequestParam(value = "estado", required = false, defaultValue = "Activo") String estado) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "redirect:/admin/categorias?msg=El nombre no puede estar vacío";
        }

        // Verifica si ya existe
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

    // 🔹 Actualizar nombre o estado
    @PostMapping("/admin/categorias/editar")
    public String editarCategoria(@RequestParam("id") Integer id,
                                  @RequestParam("nombre") String nombre,
                                  @RequestParam("estado") String estado) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "redirect:/admin/categorias?msg=El nombre no puede estar vacío";
        }

        Categoria c = new Categoria();
        c.setCategoriaId(id);;
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
