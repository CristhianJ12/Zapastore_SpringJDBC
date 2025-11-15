package com.zapastore.zapastore_h2.model.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CategoriaRepository implements CategoriaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Categoria> listarCategorias() {
        String sql = "SELECT * FROM categorias ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setCategoriaId(rs.getInt("categoria_ID"));
            c.setNombre(rs.getString("nombre"));
            c.setEstado(rs.getString("estado"));
            return c;
        });
    }

    @Override
    public List<Categoria> listarCategoriasActivas() {
        String sql = "SELECT * FROM categorias WHERE estado = 'Activo' ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setCategoriaId(rs.getInt("categoria_ID"));
            c.setNombre(rs.getString("nombre"));
            c.setEstado(rs.getString("estado"));
            return c;
        });
    }

    @Override
    public void guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre, estado) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getEstado());
    }

    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre=?, estado=? WHERE categoria_ID=?";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getEstado(),
                categoria.getCategoriaId());
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM categorias WHERE categoria_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        String sql = "SELECT * FROM categorias WHERE categoria_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Categoria c = new Categoria();
            c.setCategoriaId(rs.getInt("categoria_ID"));
            c.setNombre(rs.getString("nombre"));
            c.setEstado(rs.getString("estado"));
            return c;
        });
    }
}
