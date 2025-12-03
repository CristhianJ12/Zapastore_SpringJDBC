package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.categoria.Categoria;
import com.zapastore.zapastore_h2.model.categoria.CategoriaService;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogoClienteController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CatalogoClienteController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/cliente/catalogo")
    public String catalogo(@RequestParam(value = "categoriaId", required = false) Integer categoriaId,
                           Model model) {

        // categorías activas
        List<Categoria> categorias = categoriaService.listarCategoriasActivas();
        model.addAttribute("categorias", categorias);

        List<Producto> productos;

        if (categoriaId != null) {
            productos = productoService.buscarPorCategoriaActivos(categoriaId);
        } else {
            productos = productoService.listarProductosActivos();
        }

        // FILTRO: ocultar productos con categoría inactiva
        productos.removeIf(p -> {
            Categoria cat = categoriaService.buscarPorId(p.getCategoriaID());
            return cat == null || !"Activo".equalsIgnoreCase(cat.getEstado());
        });

        // Asignar nombre categoría
        productos.forEach(p -> {
            Categoria cat = categoriaService.buscarPorId(p.getCategoriaID());
            if (cat != null) {
                p.setCategoriaNombre(cat.getNombre());
            }
        });

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoriaId);

        return "cliente/catalogocliente";
    }

    @GetMapping("/cliente/producto/{id}")
    public String verProducto(@PathVariable Integer id, Model model) {

        Producto producto = productoService.buscarPorId(id);

        // Validación: producto activo y categoría activa
        if (producto == null || !"Activo".equalsIgnoreCase(producto.getEstado())) {
            return "redirect:/cliente/catalogo";
        }

        Categoria cat = categoriaService.buscarPorId(producto.getCategoriaID());

        if (cat == null || !"Activo".equalsIgnoreCase(cat.getEstado())) {
            return "redirect:/cliente/catalogo";
        }

        producto.setCategoriaNombre(cat.getNombre());

        List<Integer> tallas = List.of(38, 39, 40, 41, 42);
        producto.setTallas(tallas);

        model.addAttribute("producto", producto);
        return "cliente/productoDetalle";
    }
}
