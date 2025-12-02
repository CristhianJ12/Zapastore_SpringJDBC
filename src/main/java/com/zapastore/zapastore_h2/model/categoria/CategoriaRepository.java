package com.zapastore.zapastore_h2.model.categoria;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaRepository implements CategoriaDAO {

    private final JdbcTemplate jdbcTemplate;

    public CategoriaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Categoria> categoriaMapper = (rs, rowNum) ->
            new Categoria(
                    rs.getInt("categoria_ID"),
                    rs.getString("nombre"),
                    rs.getString("estado")
            );

    @Override
    public List<Categoria> listarCategorias() {
        String sql = "SELECT * FROM categorias ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, categoriaMapper);
    }

    @Override
    public List<Categoria> listarCategoriasActivas() {
        String sql = "SELECT * FROM categorias WHERE estado = 'Activo' ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, categoriaMapper);
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        String sql = "SELECT * FROM categorias WHERE categoria_ID = ?";
        List<Categoria> lista = jdbcTemplate.query(sql, categoriaMapper, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre, estado) VALUES (?, ?)";
        jdbcTemplate.update(sql, categoria.getNombre(), categoria.getEstado());
    }

    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ?, estado = ? WHERE categoria_ID = ?";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getEstado(),
                categoria.getId());
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM categorias WHERE categoria_ID = ?";
        jdbcTemplate.update(sql, id);
    }
}
