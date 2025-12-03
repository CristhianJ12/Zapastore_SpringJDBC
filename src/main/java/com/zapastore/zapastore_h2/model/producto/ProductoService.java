package com.zapastore.zapastore_h2.model.producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listarProductos();
    List<Producto> listarProductosActivos();
    List<Producto> listarProductosInactivos();   // ← NUEVO

    Producto buscarPorId(int id);
    void guardarProducto(Producto producto);
    void actualizarProducto(Producto producto);
    void desactivarProducto(int id);

    List<Producto> buscarPorNombre(String nombre);

    List<Producto> buscarPorCategoria(Integer categoriaId);
    List<Producto> buscarPorCategoriaActivos(Integer categoriaId);

    boolean existeNombre(String nombre, Integer excluirId);

    void activarProducto(int id);
}
