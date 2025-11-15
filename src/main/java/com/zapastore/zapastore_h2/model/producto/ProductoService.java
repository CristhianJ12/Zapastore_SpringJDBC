package com.zapastore.zapastore_h2.model.producto;

import java.util.List;

public interface ProductoService {
    List<Producto> listarProductos();
    Producto buscarPorId(int id);
    void guardarProducto(Producto producto);
    void actualizarProducto(Producto producto);
    void desactivarProducto(int id);
    List<Producto> buscarPorNombre(String nombre);

    // Validación de nombre repetido
    boolean existeNombre(String nombre, Integer excluirId);
}
