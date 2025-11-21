<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    
</head>
<body class="light-mode">
    <div class="page-container">

        <%@ include file="../fragments/clienteheader.jsp" %>

        <main class="main-content">
            <!-- HERO -->
            <section class="container section-padding">
                <div class="hero-banner" style='background-image: linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.5)), url("${pageContext.request.contextPath}/img/zapatilla-urb.jpg");'>
                    <div class="hero-content">
                        <h1 class="hero-title">Camina con Estilo</h1>
                        <p class="hero-subtitle">Descubre las últimas tendencias en zapatillas y lanzamientos exclusivos.</p>
                    </div>
                </div>
            </section>

            <!-- NUEVOS LANZAMIENTOS -->
            <section class="container section-padding-y">
                <h2 class="section-title">Nuevos Lanzamientos</h2>
                <div class="product-grid">
                    <c:forEach var="producto" items="${productosDestacados}">
                        <a href="${pageContext.request.contextPath}/cliente/producto/${producto.id}"
                           class="product-card-link"
                           aria-label="Ver producto: ${producto.nombre}"
                           style="text-decoration:none;color:inherit;display:block;">
                            <div class="product-card">
                                <div class="product-image-wrapper">
                                    <img alt="${producto.nombre}" class="product-image"
                                         src="${pageContext.request.contextPath}/${producto.imagenUrl}"/>
                                </div>
                                <p class="product-category">Estilo ${producto.categoriaNombre}</p>
                                <h3 class="product-title">Producto: ${producto.nombre}</h3>
                                <p class="product-detail-description">${producto.descripcion}</p>
                                <!-- El botón Comprar se remueve aquí para evitar anidar elementos clicables; la tarjeta completa es el enlace -->
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </section>

            <!-- OFERTAS DESTACADAS -->
            <!-- Ignoramos por ahora -->
        </main>

        <%@ include file="../fragments/footer.jsp" %>

    </div>
</body>
</html>
