package com.zapastore.zapastore_h2.model.producto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoServiceImpl(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
    }

    @Override
    public Producto buscarPorId(int id) {
        return productoDAO.buscarPorId(id);
    }

    @Override
    public void guardarProducto(Producto p) {

        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (productoDAO.existeNombre(p.getNombre(), null)) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }

        p.setEstado("Activo");  // estado por defecto

        productoDAO.guardar(p);
    }

    @Override
    public void actualizarProducto(Producto p) {

        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (productoDAO.existeNombre(p.getNombre(), p.getId())) {
            throw new IllegalArgumentException("Ya existe otro producto con ese nombre.");
        }

        productoDAO.actualizar(p);
    }

    @Override
    public void desactivarProducto(int id) {
        productoDAO.desactivar(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }

    @Override
    public boolean existeNombre(String nombre, Integer excluirId) {
        return productoDAO.existeNombre(nombre, excluirId);
    }
}
