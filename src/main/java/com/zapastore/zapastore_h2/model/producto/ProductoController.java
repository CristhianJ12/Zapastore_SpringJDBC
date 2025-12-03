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

    @GetMapping({"", "/lista"})
    public String listarProductos(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "showInactive", required = false, defaultValue = "false") boolean showInactive,
            Model model
    ) {
        List<Producto> productos;

        if (q != null && !q.trim().isEmpty()) {
            productos = productoService.buscarPorNombre(q);
            model.addAttribute("currentQuery", q);
        } else {
            productos = showInactive
                    ? productoService.listarProductosInactivos()
                    : productoService.listarProductosActivos();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("showInactive", showInactive);

        return "admin/productoLista";  // ← RUTA CORRECTA DEL JSP
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
                productoService.actualizarProducto(producto); // <-- ya recibe estado
            } else {
                producto.setEstado("Activo"); // siempre activo al crear
                productoService.guardarProducto(producto);
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

    @GetMapping("/activar/{id}")
    public String activarProducto(@PathVariable int id) {
        productoService.activarProducto(id);
        return "redirect:/admin/productos/lista?showInactive=true";
    }
}
