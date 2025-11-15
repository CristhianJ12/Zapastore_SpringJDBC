package com.zapastore.zapastore_h2.model.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarCategorias();
    }

    @Override
    public List<Categoria> listarCategoriasActivas() {
        return categoriaDAO.listarCategoriasActivas();
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

    @Override
    public Categoria buscarPorId(Integer id) {
        return categoriaDAO.buscarPorId(id);
    }
}
