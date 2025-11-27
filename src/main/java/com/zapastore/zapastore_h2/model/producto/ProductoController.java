package com.zapastore.zapastore_h2.model.producto;

import com.zapastore.zapastore_h2.model.categoria.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping({ "", "/lista" })
    public String listarProductos(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Producto> productos =
                (query != null && !query.isEmpty())
                        ? productoService.buscarPorNombre(query)
                        : productoService.listarProductos();

        model.addAttribute("productos", productos);
        model.addAttribute("currentQuery", query);

        return "admin/productoLista";
    }

    @GetMapping("/crear")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarCategoriasActivas());
        return "admin/formProducto";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarCategoriasActivas());
        return "admin/formProducto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, Model model) {
        Integer id = producto.getId();
        boolean esEdicion = (id != null && id > 0);

        try {
            if (esEdicion) {
                productoService.actualizarProducto(producto);
                model.addAttribute("mensaje", "Producto actualizado correctamente.");
            } else {
                productoService.guardarProducto(producto);
                model.addAttribute("mensaje", "Producto registrado exitosamente.");
            }
            return "redirect:/admin/productos/lista";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categorias", categoriaService.listarCategoriasActivas());
            return "admin/formProducto";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String desactivarProducto(@PathVariable int id) {
        productoService.desactivarProducto(id);
        return "redirect:/admin/productos/lista";
    }
}