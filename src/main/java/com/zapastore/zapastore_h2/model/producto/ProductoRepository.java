package com.zapastore.zapastore_h2.model.producto;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductoRepository implements ProductoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Producto> mapper = new RowMapper<>() {
        @Override
        public Producto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Producto p = new Producto();
            p.setId(rs.getInt("producto_ID"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getBigDecimal("Precio"));
            p.setImagenUrl(rs.getString("img_Url"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEstado(rs.getString("estado"));

            Object catObj = rs.getObject("categoria_ID");
            p.setCategoriaID(catObj == null ? null : ((Number) catObj).intValue());

            p.setCategoriaNombre(rs.getString("categoria_nombre"));
            return p;
        }
    };

    @Override
    public List<Producto> listarProductos() {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            ORDER BY p.producto_ID ASC
        """;
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Producto> listarProductosActivos() {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE p.estado = 'Activo'
            ORDER BY p.producto_ID ASC
        """;
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Producto> listarProductosInactivos() {
        String sql = """
        SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
               p.descripcion, p.estado, p.categoria_ID,
               c.nombre AS categoria_nombre
        FROM productos p
        LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
        WHERE p.estado = 'Inactivo'
        ORDER BY p.producto_ID ASC
    """;
        return jdbcTemplate.query(sql, mapper);
    }


    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE LOWER(p.nombre) LIKE LOWER(?)
            ORDER BY p.producto_ID ASC
        """;
        String param = "%" + (nombre == null ? "" : nombre.trim()) + "%";
        return jdbcTemplate.query(sql, mapper, param);
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE p.producto_ID = ?
        """;
        List<Producto> lista = jdbcTemplate.query(sql, mapper, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void guardar(Producto p) {
        String sql = """
            INSERT INTO productos (nombre, Precio, img_Url, descripcion, estado, categoria_ID)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                p.getNombre(),
                p.getPrecio(),
                p.getImagenUrl(),
                p.getDescripcion(),
                p.getEstado(),
                p.getCategoriaID()
        );
    }

    @Override
    public void actualizar(Producto p) {
        String sql = """
        UPDATE productos 
        SET nombre=?, Precio=?, img_Url=?, descripcion=?, categoria_ID=?, estado=?
        WHERE producto_ID=?
    """;

        jdbcTemplate.update(sql,
                p.getNombre(),
                p.getPrecio(),
                p.getImagenUrl(),
                p.getDescripcion(),
                p.getCategoriaID(),
                p.getEstado(),
                p.getId()
        );
    }

    @Override
    public void desactivar(int id) {
        jdbcTemplate.update("UPDATE productos SET estado='Inactivo' WHERE producto_ID=?", id);
    }

    @Override
    public boolean existeNombre(String nombre, Integer excluirId) {
        String sql = "SELECT producto_ID FROM productos WHERE LOWER(nombre) = LOWER(?)";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("producto_ID"), nombre);

        if (ids.isEmpty()) return false;
        if (excluirId == null) return true;

        return ids.stream().anyMatch(id -> !Objects.equals(id, excluirId));
    }

    @Override
    public List<Producto> buscarPorCategoria(Integer categoriaId) {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE p.categoria_ID = ?
            ORDER BY p.producto_ID ASC
        """;
        return jdbcTemplate.query(sql, mapper, categoriaId);
    }

    @Override
    public List<Producto> buscarPorCategoriaActivos(Integer categoriaId) {
        String sql = """
            SELECT p.producto_ID, p.nombre, p.Precio, p.img_Url,
                   p.descripcion, p.estado, p.categoria_ID,
                   c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE p.categoria_ID = ? AND p.estado = 'Activo'
            ORDER BY p.producto_ID ASC
        """;
        return jdbcTemplate.query(sql, mapper, categoriaId);
    }

    @Override
    public void activar(int id) {
        jdbcTemplate.update("UPDATE productos SET estado='Activo' WHERE producto_ID=?", id);
    }

}