/*package com.zapastore.zapastore_h2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zapastore.zapastore_h2.model.categoria.Categoria;
import com.zapastore.zapastore_h2.model.categoria.CategoriaDAO;

@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriaController {

    private final CategoriaDAO categoriaDAO;

    public AdminCategoriaController(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaDAO.listar());
        return "admin/categoriaLista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categoriaEditar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        if (categoria.getCategoria_ID() == 0) {
            categoriaDAO.guardar(categoria);
        } else {
            categoriaDAO.actualizar(categoria);
        }
        return "redirect:/admin/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("categoria", categoriaDAO.buscarPorId(id));
        return "admin/categoriaEditar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        categoriaDAO.eliminar(id);
        return "redirect:/admin/categorias";
    }
}*/
