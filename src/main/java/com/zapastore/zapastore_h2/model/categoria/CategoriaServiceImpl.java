package com.zapastore.zapastore_h2.model.categoria;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarCategorias();
    }

    @Override
    public List<Categoria> listarCategoriasActivas() {
        return categoriaDAO.listarCategoriasActivas();
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        return categoriaDAO.buscarPorId(id);
    }

    @Override
    public void guardar(Categoria categoria) {
        categoriaDAO.guardar(categoria);
    }

    @Override
    public void actualizar(Categoria categoria) {
        categoriaDAO.actualizar(categoria);
    }

    @Override
    public void eliminar(Integer id) {
        categoriaDAO.eliminar(id);
    }
}
