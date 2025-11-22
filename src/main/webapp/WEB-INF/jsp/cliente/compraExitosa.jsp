<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Compra Exitosa - Pedido #${pedidoExitoso.id}</title>
    <link rel="stylesheet" href="${ctx}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
</head>
<body class="light-mode">
<div class="page-container">
    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container section-padding success-section">
            <h1 class="section-title success-heading">
                <i class="fa-solid fa-check-circle"></i> ¡Compra Exitosa!
            </h1>
            <p class="success-message">Tu pedido **#<c:out value="${pedidoExitoso.id}"/>** ha sido procesado. Los detalles se muestran a continuación.</p>

            <div class="order-summary-box card">
                <h2>Resumen del Pedido</h2>
                <%-- ✅ CORRECCIÓN FECHA --%>
                <p><strong>Fecha:</strong> <fmt:formatDate value="${pedidoExitoso.fechaTimestamp}" pattern="dd/MM/yyyy HH:mm"/></p>
                <p><strong>Estado:</strong> <span class="status-completed"><c:out value="${pedidoExitoso.estado}"/></span></p>

                <%-- ❌ LÍNEA DE CLIENTE ELIMINADA: Esto resuelve el error PropertyNotFoundException. --%>

                <h3>Detalles de Productos</h3>
                <table class="order-details-table">
                    <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Talla</th>
                        <th>Cantidad</th>
                        <th>Precio Unitario</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="detalle" items="${pedidoExitoso.detalles}">
                        <tr>
                            <td><c:out value="${detalle.nombreProducto}"/></td>
                            <td><c:out value="${detalle.talla}"/></td>
                            <td><c:out value="${detalle.cantidad}"/></td>
                            <td>S/ <fmt:formatNumber value="${detalle.precioUnitario}" minFractionDigits="2"/></td>
                            <td>S/ <fmt:formatNumber value="${detalle.subtotal}" minFractionDigits="2"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="order-totals">
                    <p>Costo de Envío: S/ <fmt:formatNumber value="${pedidoExitoso.costoEnvio}" minFractionDigits="2"/></p>
                    <p class="total-final">Total a Pagar: **S/ <fmt:formatNumber value="${pedidoExitoso.totalPagar}" minFractionDigits="2"/>**</p>
                </div>
            </div>

            <a href="${ctx}/cliente/home" class="primary-button mt-4">Volver al Inicio</a>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>
</div>

<script>
    // SOLUCIÓN para prevenir el reenvío/recarga con el caché del navegador (Back/Forward)
    if (window.history.replaceState) {
        window.history.replaceState(null, null, '${ctx}/cliente/home');
    }
</script>

</body>
</html>