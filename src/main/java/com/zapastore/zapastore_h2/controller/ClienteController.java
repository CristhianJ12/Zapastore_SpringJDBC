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
public class ClienteController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ClienteController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/cliente/home")
    public String home(Model model) {

        List<Producto> productos = productoService.listarProductos();

        // Agrupar productos por categoriaID y tomar el último de cada categoría
        Map<Integer, Producto> ultimoProductoPorCategoria = productos.stream()
                .filter(p -> p.getCategoriaID() != null)
                .collect(Collectors.toMap(
                        Producto::getCategoriaID,
                        p -> p,
                        (p1, p2) -> p2
                ));

        // Asignar nombre de categoría a cada producto
        ultimoProductoPorCategoria.values().forEach(p -> {
            Categoria categoria = categoriaService.buscarPorId(p.getCategoriaID());
            if (categoria != null) {
                p.setCategoriaNombre(categoria.getNombre());
            }
        });

        model.addAttribute("productosDestacados", new ArrayList<>(ultimoProductoPorCategoria.values()));

        return "cliente/homecliente";
    }

    @GetMapping("/cliente/contacto")
    public String contacto() {
        return "cliente/contactocliente";
    }

    @GetMapping("/cliente/ofertas")
    public String ofertas() {
        return "cliente/ofertascliente";
    }
}