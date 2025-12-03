package com.zapastore.zapastore_h2.controller;

import com.zapastore.zapastore_h2.model.categoria.Categoria;
import com.zapastore.zapastore_h2.model.categoria.CategoriaService;
import com.zapastore.zapastore_h2.model.producto.Producto;
import com.zapastore.zapastore_h2.model.producto.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public HomeController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {

        // *** NUEVO: Filtrar categorías activas ***
        List<Categoria> categoriasActivas = categoriaService.listarCategoriasActivas();
        Set<Integer> idsCategoriasActivas = categoriasActivas.stream()
                .map(Categoria::getId)
                .collect(Collectors.toSet());

        // Usar solo productos activos que pertenezcan a categorías activas
        List<Producto> productos = productoService.listarProductosActivos().stream()
                .filter(p -> idsCategoriasActivas.contains(p.getCategoriaID()))
                .collect(Collectors.toList());

        // Agrupar productos por categoriaID y tomar el último de cada categoría
        Map<Integer, Producto> ultimoProductoPorCategoria = productos.stream()
                .filter(p -> p.getCategoriaID() != null)
                .collect(Collectors.toMap(
                        Producto::getCategoriaID,
                        p -> p,
                        (p1, p2) -> p2
                ));

        // Asignar nombre de categoría
        ultimoProductoPorCategoria.values().forEach(p -> {
            var categoria = categoriaService.buscarPorId(p.getCategoriaID());
            if (categoria != null) {
                p.setCategoriaNombre(categoria.getNombre());
            }
        });

        model.addAttribute("productosDestacados",
                new ArrayList<>(ultimoProductoPorCategoria.values()));

        return "home";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/ofertas")
    public String ofertas() {
        return "ofertas";
    }
}
