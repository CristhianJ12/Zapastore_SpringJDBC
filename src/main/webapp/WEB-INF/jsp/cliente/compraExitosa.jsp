<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Compra Exitosa</title>
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

    <section class="container section-padding success-section">
        <h1>¡Compra Exitosa!</h1>
        <p>Tu pedido ha sido completado y ya puedes ir a recogerlo en tienda.</p>
        <a href="${pageContext.request.contextPath}/cliente/home" class="primary-button">Volver al Inicio</a>
    </section>

    <%@ include file="../fragments/footer.jsp" %>
</div>
</body>
</html>
