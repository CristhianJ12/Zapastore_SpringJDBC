package com.zapastore.zapastore_h2.controller;

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

        // CAMBIO: Usar solo productos activos
        List<Producto> productos = productoService.listarProductosActivos();

        // Agrupar productos por categoriaID y tomar el último de cada categoría
        Map<Integer, Producto> ultimoProductoPorCategoria = productos.stream()
                .filter(p -> p.getCategoriaID() != null)
                .collect(Collectors.toMap(
                        Producto::getCategoriaID,   // Clave = categoriaID
                        p -> p,                     // Valor = el producto
                        (p1, p2) -> p2              // En caso de duplicado, tomar el último
                ));

        // Asignar nombre de categoría a cada producto
        ultimoProductoPorCategoria.values().forEach(p -> {
            var categoria = categoriaService.buscarPorId(p.getCategoriaID());
            if (categoria != null) {
                p.setCategoriaNombre(categoria.getNombre());
            }
        });

        // Pasar la lista al modelo
        model.addAttribute("productosDestacados", new ArrayList<>(ultimoProductoPorCategoria.values()));

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