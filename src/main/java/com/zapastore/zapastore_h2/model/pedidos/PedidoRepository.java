package com.zapastore.zapastore_h2.model.pedidos;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import com.zapastore.zapastore_h2.model.usuarios.UsuarioDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepository implements PedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioDAO usuarioDAO;

    public PedidoRepository(JdbcTemplate jdbcTemplate, UsuarioDAO usuarioDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioDAO = usuarioDAO;
    }

    private final RowMapper<Pedido> pedidoRowMapper = new RowMapper<>() {
        @Override
        public Pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pedido p = new Pedido();
            p.setId(rs.getInt("pedido_ID"));
            p.setTotalPagar(rs.getBigDecimal("total_pagar"));
            p.setCostoEnvio(rs.getBigDecimal("CostoEnvio"));
            p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
            p.setEstado(rs.getString("estado"));

            String idCliente = rs.getString("IDCliente");
            usuarioDAO.buscarPorId(idCliente).ifPresent(p::setCliente);

            return p;
        }
    };

    @Override
    public Pedido save(Pedido pedido) {
        if (pedido.getId() != null && pedido.getId() > 0) {
            String sql = "UPDATE pedidos SET IDCliente=?, total_pagar=?, CostoEnvio=?, fecha=?, estado=? WHERE pedido_ID=?";
            jdbcTemplate.update(sql,
                    pedido.getCliente().getIdUsuario(),
                    pedido.getTotalPagar(),
                    pedido.getCostoEnvio(),
                    Timestamp.valueOf(pedido.getFecha()),
                    pedido.getEstado(),
                    pedido.getId());
        } else {
            String sql = "INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, pedido.getCliente().getIdUsuario());
                ps.setBigDecimal(2, pedido.getTotalPagar());
                ps.setBigDecimal(3, pedido.getCostoEnvio());
                ps.setTimestamp(4, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(5, pedido.getEstado());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                pedido.setId(key.intValue());
            }
        }
        return pedido;
    }

    @Override
    public List<Pedido> findByCliente(Usuario cliente) {
        String sql = "SELECT * FROM pedidos WHERE IDCliente=?";
        return jdbcTemplate.query(sql, pedidoRowMapper, cliente.getIdUsuario());
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        String sql = "SELECT * FROM pedidos WHERE pedido_ID=?";
        List<Pedido> list = jdbcTemplate.query(sql, pedidoRowMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM pedidos WHERE pedido_ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Pedido> findByClienteAndEstado(Usuario cliente, String estado) {
        String sql = "SELECT * FROM pedidos WHERE IDCliente=? AND estado=?";
        return jdbcTemplate.query(sql, pedidoRowMapper, cliente.getIdUsuario(), estado);
    }

    @Override
    public List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT * FROM pedidos WHERE fecha >= ? AND fecha <= ?";
        return jdbcTemplate.query(sql, pedidoRowMapper, Timestamp.valueOf(inicio), Timestamp.valueOf(fin));
    }

    @Override
    public void actualizarTotalPagar(Integer pedidoId, BigDecimal nuevoTotal) {
        String sql = "UPDATE pedidos SET total_pagar=? WHERE pedido_ID=?";
        jdbcTemplate.update(sql, nuevoTotal, pedidoId);
    }

    @Override
    public void actualizarEstadoYFecha(Integer pedidoId, String nuevoEstado, LocalDateTime fecha) {
        String sql = "UPDATE pedidos SET estado=?, fecha=? WHERE pedido_ID=?";
        Timestamp fechaTimestamp = Timestamp.valueOf(fecha);
        jdbcTemplate.update(sql, nuevoEstado, fechaTimestamp, pedidoId);
    }

    @Override
    public List<Pedido> findCompletadosByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT * FROM pedidos WHERE fecha >= ? AND fecha <= ? AND estado = 'Completado'";
        return jdbcTemplate.query(sql, pedidoRowMapper, Timestamp.valueOf(inicio), Timestamp.valueOf(fin));
    }

    @Override
    public BigDecimal calcularTotalVentasPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(total_pagar), 0) FROM pedidos WHERE fecha >= ? AND fecha <= ? AND estado = 'Completado'";
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class, Timestamp.valueOf(inicio), Timestamp.valueOf(fin));
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public int contarPedidosCompletadosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE fecha >= ? AND fecha <= ? AND estado = 'Completado'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, Timestamp.valueOf(inicio), Timestamp.valueOf(fin));
        return count != null ? count : 0;
    }
}