package com.zapastore.zapastore_h2.model.producto;

import java.util.List;

public interface ProductoDAO {

    List<Producto> listarProductos();
    List<Producto> buscarPorNombre(String nombre);
    Producto buscarPorId(int id);
    void guardar(Producto producto);
    void actualizar(Producto producto);
    void desactivar(int id);
    boolean existeNombre(String nombre, Integer excluirId);
    List<Producto> buscarPorCategoria(Integer categoriaId);
}