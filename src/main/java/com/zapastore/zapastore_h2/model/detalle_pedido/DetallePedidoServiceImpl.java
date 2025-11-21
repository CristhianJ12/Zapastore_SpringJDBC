    package com.zapastore.zapastore_h2.model.detalle_pedido;

    import com.zapastore.zapastore_h2.model.producto.Producto;
    import com.zapastore.zapastore_h2.model.producto.ProductoService;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class DetallePedidoServiceImpl implements DetallePedidoService {

        private final DetallePedidoDAO detalleDAO;
        private final ProductoService productoService;

        public DetallePedidoServiceImpl(DetallePedidoDAO detalleDAO, ProductoService productoService) {
            this.detalleDAO = detalleDAO;
            this.productoService = productoService;
        }

        @Override
        public List<DetallePedido> listarPorPedido(Integer pedidoId) {
            List<DetallePedido> lista = detalleDAO.listarPorPedido(pedidoId);

            lista.forEach(d -> d.setProducto(
                    productoService.buscarPorId(d.getProductoId())
            ));

            return lista;
        }

        @Override
        public DetallePedido buscarPorId(Integer id) {
            DetallePedido d = detalleDAO.buscarPorId(id);
            if (d != null) {
                d.setProducto(productoService.buscarPorId(d.getProductoId()));
            }
            return d;
        }

        @Override
        public void guardar(DetallePedido detalle) {
            detalleDAO.guardar(detalle);
        }

        @Override
        public void actualizar(DetallePedido detalle) {
            detalleDAO.actualizar(detalle);
        }

        @Override
        public void eliminar(Integer id) {
            detalleDAO.eliminar(id);
        }
    }

