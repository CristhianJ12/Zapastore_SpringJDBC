package com.zapastore.zapastore_h2.model.producto;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.listarProductos();
    }

    @Override
    public Producto buscarPorId(int id) {
        return productoRepository.buscarPorId(id);
    }

    @Override
    public void guardarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (existeNombre(producto.getNombre(), null)) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }

        productoRepository.guardarProducto(producto);
    }

    @Override
    public void actualizarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        if (existeNombre(producto.getNombre(), producto.getId())) {
            throw new IllegalArgumentException("Ya existe otro producto con ese nombre.");
        }

        productoRepository.actualizarProducto(producto);
    }

    @Override
    public void desactivarProducto(int id) {
        productoRepository.desactivarProducto(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre);
    }

    @Override
    public boolean existeNombre(String nombre, Integer excluirId) {
        List<Producto> productos = productoRepository.buscarPorNombre(nombre);
        return productos.stream()
                .anyMatch(p -> excluirId == null || p.getId() != excluirId);
    }
}
