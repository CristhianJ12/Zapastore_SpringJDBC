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

        // Obtener todas las categorías activas para el select
        List<Categoria> categorias = categoriaService.listarCategoriasActivas();
        model.addAttribute("categorias", categorias);

        // Obtener productos filtrados por categoría o todos
        List<Producto> productos;
        if (categoriaId != null) {
            productos = productoService.buscarPorCategoria(categoriaId);
        } else {
            productos = productoService.listarProductos();
        }

        // Asignar nombre de categoría a cada producto
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
        // Buscar el producto por ID
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }

        // Obtener el nombre de la categoría
        Categoria cat = categoriaService.buscarPorId(producto.getId());
        if (cat != null) {
            producto.setCategoriaNombre(cat.getNombre());
        }

        // Lista de tallas disponibles (puede ser fija o dinámica)
        List<Integer> tallas = List.of(38, 39, 40, 41, 42);
        producto.setTallas(tallas);

        model.addAttribute("producto", producto);

        return "cliente/productoDetalle";
    }
}
