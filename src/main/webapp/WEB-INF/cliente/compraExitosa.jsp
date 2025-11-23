<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compra Exitosa - Pedido #${pedidoExitoso.id} | ZapaStore</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/index.css">
</head>
<body class="light-mode">
<div class="page-container">
    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container section-padding success-section">
            <h1 class="section-title success-heading">
                <i class="fa-solid fa-check-circle" aria-hidden="true"></i> ¡Compra Exitosa!
            </h1>
            <p class="success-message">
                Tu pedido <strong>#<c:out value="${pedidoExitoso.id}" /></strong> ha sido procesado.
                Los detalles se muestran a continuación.
            </p>

            <article class="order-summary-box card">
                <h2>Resumen del Pedido</h2>
                <dl class="order-info">
                    <dt>Fecha:</dt>
                    <dd><fmt:formatDate value="${pedidoExitoso.fechaTimestamp}" pattern="dd/MM/yyyy HH:mm" /></dd>

                    <dt>Estado:</dt>
                    <dd><span class="status-completed"><c:out value="${pedidoExitoso.estado}" /></span></dd>
                </dl>

                <h3>Detalles de Productos</h3>
                <div class="table-responsive">
                    <table class="order-details-table">
                        <thead>
                        <tr>
                            <th scope="col">Producto</th>
                            <th scope="col">Talla</th>
                            <th scope="col">Cantidad</th>
                            <th scope="col">Precio Unitario</th>
                            <th scope="col">Subtotal</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="detalle" items="${pedidoExitoso.detalles}">
                            <tr>
                                <td data-label="Producto"><c:out value="${detalle.nombreProducto}" /></td>
                                <td data-label="Talla"><c:out value="${detalle.talla}" /></td>
                                <td data-label="Cantidad"><c:out value="${detalle.cantidad}" /></td>
                                <td data-label="Precio Unitario">S/ <fmt:formatNumber value="${detalle.precioUnitario}" minFractionDigits="2" /></td>
                                <td data-label="Subtotal">S/ <fmt:formatNumber value="${detalle.subtotal}" minFractionDigits="2" /></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="order-totals">
                    <p>Costo de Envío: <strong>S/ <fmt:formatNumber value="${pedidoExitoso.costoEnvio}" minFractionDigits="2" /></strong></p>
                    <p class="total-final">Total a Pagar: <strong>S/ <fmt:formatNumber value="${pedidoExitoso.totalPagar}" minFractionDigits="2" /></strong></p>
                </div>
            </article>

            <a href="${ctx}/cliente/home" class="primary-button mt-4">Volver al Inicio</a>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>
</div>

<script>
    if (window.history.replaceState) {
        window.history.replaceState(null, null, '${ctx}/cliente/home');
    }
</script>

</body>
</html>