package com.zapastore.zapastore_h2.model.producto;

import java.util.List;

public interface ProductoDAO {
    List<Producto> listarProductos();
    Producto buscarPorId(int id);
    void guardar(Producto producto);
    void actualizar(Producto producto);
    void eliminar(int id);
}

