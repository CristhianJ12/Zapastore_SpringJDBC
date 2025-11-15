package com.zapastore.zapastore_h2.model.producto;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Producto> productoMapper = new RowMapper<>() {
        @Override
        public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Producto p = new Producto();
            p.setId(rs.getInt("producto_ID"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("Precio"));
            p.setImagenUrl(rs.getString("img_Url"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEstado(rs.getString("estado")); // ← tu modelo usa String ("Activo"/"Inactivo")
            p.setCategoriaID(rs.getInt("categoria_ID"));
            p.setCategoriaNombre(rs.getString("categoria_nombre"));
            return p;
        }
    };

    // 🔹 Listar productos
    public List<Producto> listarProductos() {
        String sql = """
            SELECT 
                p.producto_ID, p.nombre, p.Precio, p.img_Url, 
                p.descripcion, p.estado, p.categoria_ID,
                c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            ORDER BY p.producto_ID ASC
        """;
        return jdbcTemplate.query(sql, productoMapper);
    }

    // 🔹 Buscar producto por ID
    public Producto buscarPorId(int id) {
        String sql = """
            SELECT 
                p.producto_ID, p.nombre, p.Precio, p.img_Url, 
                p.descripcion, p.estado, p.categoria_ID,
                c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE p.producto_ID = ?
        """;
        List<Producto> lista = jdbcTemplate.query(sql, productoMapper, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // 🔹 Buscar por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        String sql = """
            SELECT 
                p.producto_ID, p.nombre, p.Precio, p.img_Url, 
                p.descripcion, p.estado, p.categoria_ID,
                c.nombre AS categoria_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_ID = c.categoria_ID
            WHERE LOWER(p.nombre) = LOWER(?)
        """;
        return jdbcTemplate.query(sql, productoMapper, nombre);
    }

    // 🔹 Guardar nuevo producto
    public void guardarProducto(Producto p) {
        String sql = """
            INSERT INTO productos (nombre, Precio, img_Url, descripcion, estado, categoria_ID)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                p.getNombre(),
                p.getPrecio(),
                p.getImagenUrl(),
                p.getDescripcion(),
                "Activo",
                p.getCategoriaID()
        );
    }

    // 🔹 Actualizar producto existente
    public void actualizarProducto(Producto p) {
        String sql = """
            UPDATE productos
            SET nombre = ?, Precio = ?, img_Url = ?, descripcion = ?, categoria_ID = ?
            WHERE producto_ID = ?
        """;
        jdbcTemplate.update(sql,
                p.getNombre(),
                p.getPrecio(),
                p.getImagenUrl(),
                p.getDescripcion(),
                p.getCategoriaID(),
                p.getId()
        );
    }

    // 🔹 Desactivar (cambiar estado)
    public void desactivarProducto(int id) {
        String sql = "UPDATE productos SET estado = 'Inactivo' WHERE producto_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    
}
