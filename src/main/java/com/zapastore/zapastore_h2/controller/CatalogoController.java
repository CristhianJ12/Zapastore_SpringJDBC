package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.categoria.Categoria;
import com.zapastore.zapastore_h2.model.categoria.CategoriaService;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CatalogoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(value = "categoriaId", required = false) Integer categoriaId,
                           Model model) {

        // Obtener solo categorías ACTIVAS
        List<Categoria> categorias = categoriaService.listarCategoriasActivas();
        model.addAttribute("categorias", categorias);

        // Obtener productos activos según categoría
        List<Producto> productos;

        if (categoriaId != null) {
            productos = productoService.buscarPorCategoriaActivos(categoriaId);
        } else {
            productos = productoService.listarProductosActivos();
        }

        // FILTRO EXTRA: eliminar productos cuya categoría está inactiva
        productos.removeIf(p -> {
            Categoria cat = categoriaService.buscarPorId(p.getCategoriaID());
            return cat == null || !"Activo".equalsIgnoreCase(cat.getEstado());
        });

        // Asignar nombre de categoría
        productos.forEach(p -> {
            Categoria cat = categoriaService.buscarPorId(p.getCategoriaID());
            if (cat != null) {
                p.setCategoriaNombre(cat.getNombre());
            }
        });

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoriaId);

        return "catalogo";
    }
}
