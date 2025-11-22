package com.zapastore.zapastore_h2.model.detalle_pedido;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DetallePedidoRepository implements DetallePedidoDAO {

    private final JdbcTemplate jdbc;

    // Definición explícita de columnas
    private static final String SELECT_COLUMNS =
            "pedidodetalle_ID, pedido_ID, producto_ID, cantidad, talla, precio_unitario, subtotal, nombre_producto";

    public DetallePedidoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<DetallePedido> mapper = (rs, rowNum) -> {
        DetallePedido detalle = new DetallePedido();
        detalle.setId(rs.getInt("pedidodetalle_ID"));
        detalle.setPedidoId(rs.getInt("pedido_ID"));
        detalle.setProductoId(rs.getInt("producto_ID"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setTalla(rs.getInt("talla"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        detalle.setNombreProducto(rs.getString("nombre_producto"));
        return detalle;
    };

    @Override
    public List<DetallePedido> listarPorPedido(Integer pedidoId) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM pedido_detalle WHERE pedido_ID=?";
        System.out.println("DEBUG REPO DETALLE: Ejecutando SELECT: " + sql + " con ID: " + pedidoId);
        List<DetallePedido> detalles = jdbc.query(sql, mapper, pedidoId);
        System.out.println("DEBUG REPO DETALLE: Filas mapeadas: " + detalles.size());
        return detalles;
    }

    @Override
    public DetallePedido buscarPorId(Integer id) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM pedido_detalle WHERE pedidodetalle_ID=?";
        List<DetallePedido> lista = jdbc.query(sql, mapper, id);
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
        // 🚨 CORRECCIÓN CRÍTICA: La sentencia SQL ahora coincide con los 5 parámetros (cantidad, talla,
        // precio_unitario, subtotal, nombre_producto) antes del WHERE.
        jdbc.update("""
            UPDATE pedido_detalle SET 
            cantidad=?, 
            talla=?, 
            precio_unitario=?, 
            subtotal=?, 
            nombre_producto=?
            WHERE pedidodetalle_ID=?
        """,
                d.getCantidad(),
                d.getTalla(),
                d.getPrecioUnitario(),
                d.getSubtotal(),
                d.getNombreProducto(),
                d.getId()
        );
    }

    @Override
    public void eliminar(Integer id) {
        jdbc.update("DELETE FROM pedido_detalle WHERE pedidodetalle_ID=?", id);
    }

    @Override
    public DetallePedido buscarPorPedidoProductoYTalla(Integer pedidoId, Integer productoId, Integer talla) {
        String sql = "SELECT " + SELECT_COLUMNS + """
        FROM pedido_detalle
        WHERE pedido_ID = ? AND producto_ID = ? AND talla = ?
    """;

        List<DetallePedido> lista = jdbc.query(sql, mapper, pedidoId, productoId, talla);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public BigDecimal calcularSubtotalPorPedido(Integer pedidoId) {
        String sql = "SELECT SUM(subtotal) FROM pedido_detalle WHERE pedido_ID = ?";
        BigDecimal total = jdbc.queryForObject(sql, BigDecimal.class, pedidoId);
        return total != null ? total : BigDecimal.ZERO;
    }
}