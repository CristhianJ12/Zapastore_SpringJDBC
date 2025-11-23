<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil - Pedidos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
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
                </c:when>
                <c:otherwise>
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>ID Pedido</th>
                                <th>Fecha</th>
                                <th>Total a Pagar</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="pedido" items="${pedidos}">
                                <tr>
                                    <td>${pedido.id}</td>
                                    <td>${pedido.fecha}</td>
                                    <td>S/ ${pedido.totalPagar}</td>
                                    <td class="<c:out value='${pedido.estado.toLowerCase()}'/>">${pedido.estado}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/cliente/home" class="secondary-link">Volver al Inicio</a>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>
</div>
</body>
</html>
