<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - Pedidos | ZapaStore</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/index.css">
</head>
<body class="light-mode">
<div class="page-container">
    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container section-padding">
            <h1>Mis Pedidos Completados</h1>

            <c:choose>
                <c:when test="${empty pedidos}">
                    <p>No tienes pedidos completados aún.</p>
                    <a href="${ctx}/cliente/home" class="primary-button">Ir a Comprar</a>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="orders-table">
                            <thead>
                            <tr>
                                <th scope="col">ID Pedido</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Total a Pagar</th>
                                <th scope="col">Estado</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="pedido" items="${pedidos}">
                                <tr>
                                    <td data-label="ID Pedido"><c:out value="${pedido.id}" /></td>
                                    <td data-label="Fecha"><c:out value="${pedido.fecha}" /></td>
                                    <td data-label="Total a Pagar">S/ <fmt:formatNumber value="${pedido.totalPagar}" minFractionDigits="2" /></td>
                                    <td data-label="Estado" class="${pedido.estado == 'Completado' ? 'status-completed' : ''}">
                                        <c:out value="${pedido.estado}" />
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <a href="${ctx}/cliente/home" class="secondary-link">Volver al Inicio</a>
                </c:otherwise>
            </c:choose>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>
</div>
</body>
</html>