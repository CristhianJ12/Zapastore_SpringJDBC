package com.zapastore.zapastore_h2.model.categoria;

import java.util.List;

public interface CategoriaDAO {

    public List<Categoria> listarCategorias();

    List<Categoria> listarCategoriasActivas();

    Categoria buscarPorId(Integer id);

    void guardar(Categoria categoria);

    void actualizar(Categoria categoria);

    void eliminar(Integer id);
}
