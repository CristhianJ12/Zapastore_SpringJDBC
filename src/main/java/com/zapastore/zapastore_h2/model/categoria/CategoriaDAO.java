package com.zapastore.zapastore_h2.model.categoria;

import java.util.List;

public interface CategoriaDAO {
    List<Categoria> listarCategorias();
    List<Categoria> listarCategoriasActivas();

    void guardar(Categoria categoria);
    void actualizar(Categoria categoria);
    void eliminar(Integer id);

    Categoria buscarPorId(Integer id);
}
