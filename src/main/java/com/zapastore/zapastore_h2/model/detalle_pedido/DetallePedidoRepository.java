package com.zapastore.zapastore_h2.model.detalle_pedido;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DetallePedidoRepository implements DetallePedidoDAO {

    private final JdbcTemplate jdbc;

    public DetallePedidoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<DetallePedido> mapper = (rs, rowNum) ->
            new DetallePedido(
                    rs.getInt("pedidodetalle_ID"),
                    rs.getInt("pedido_ID"),
                    rs.getInt("producto_ID"),
                    rs.getInt("cantidad"),
                    rs.getInt("talla"),
                    rs.getBigDecimal("precio_unitario"),
                    rs.getBigDecimal("subtotal"),
                    rs.getString("nombre_producto")
            );

    @Override
    public List<DetallePedido> listarPorPedido(Integer pedidoId) {
        return jdbc.query("SELECT * FROM pedido_detalle WHERE pedido_ID=?", mapper, pedidoId);
    }

    @Override
    public DetallePedido buscarPorId(Integer id) {
        List<DetallePedido> lista = jdbc.query(
                "SELECT * FROM pedido_detalle WHERE pedidodetalle_ID=?",
                mapper, id
        );
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void guardar(DetallePedido d) {
        jdbc.update("""
            INSERT INTO pedido_detalle 
            (pedido_ID, producto_ID, cantidad, talla, precio_unitario, subtotal, nombre_producto)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """,
                d.getPedidoId(), d.getProductoId(), d.getCantidad(),
                d.getTalla(), d.getPrecioUnitario(), d.getSubtotal(),
                d.getNombreProducto()
        );
    }

    @Override
    public void actualizar(DetallePedido d) {
        jdbc.update("""
            UPDATE pedido_detalle SET 
            cantidad=?, talla=?, precio_unitario=?, subtotal=?, nombre_producto=?
            WHERE pedidodetalle_ID=?
        """,
                d.getCantidad(), d.getTalla(), d.getPrecioUnitario(),
                d.getSubtotal(), d.getNombreProducto(), d.getId()
        );
    }

    @Override
    public void eliminar(Integer id) {
        jdbc.update("DELETE FROM pedido_detalle WHERE pedidodetalle_ID=?", id);
    }
    public DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla) {
        String sql = """
        SELECT * FROM pedido_detalle
        WHERE pedido_ID = ? AND producto_ID = ? AND talla = ?
    """;

        List<DetallePedido> lista = jdbc.query(sql, mapper, pedidoId, productoId, talla);
        return lista.isEmpty() ? null : lista.get(0);
    }

}
