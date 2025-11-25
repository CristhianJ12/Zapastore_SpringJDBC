<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore</title>

    <!-- Fuentes y estilos -->
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>

<body class="light-mode">
<div class="page-container">

    <%@ include file="fragments/userheader.jsp" %>

    <main class="main-content">

        <!-- HERO -->
        <section class="container section-padding">
            <div class="hero-banner"
                 style='background-image: linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.5)),
                 url("${pageContext.request.contextPath}/img/zapatilla-urb.jpg");'>

                <div class="hero-content">
                    <h1 class="hero-title">Camina con Estilo</h1>

                    <p class="hero-subtitle">
                        Descubre las últimas tendencias en zapatillas y lanzamientos exclusivos.
                    </p>

                    <button type="button"
                            class="primary-button"
                            onclick="window.location.href='${pageContext.request.contextPath}/login'">
                        Comprar ahora
                    </button>
                </div>
            </div>
        </section>

        <!-- NUEVOS LANZAMIENTOS -->
        <section class="container section-padding-y">
            <h2 class="section-title">Nuevos Lanzamientos</h2>

            <div class="product-grid">
                <c:forEach var="producto" items="${productosDestacados}">

                    <div class="product-card">

                        <div class="product-image-wrapper">
                            <img
                                    class="product-image"
                                    src="${pageContext.request.contextPath}/${producto.imagenUrl}"
                                    alt="<c:out value='${producto.nombre}'/>">
                        </div>

                        <p class="product-category">
                            Estilo: <c:out value="${producto.categoriaNombre}"/>
                        </p>

                        <h3 class="product-title">
                            <c:out value="${producto.nombre}"/>
                        </h3>

                    </div>

                </c:forEach>
            </div>
        </section>

        <!-- OFERTAS DESTACADAS (pendiente) -->

    </main>

    <%@ include file="fragments/footer.jsp" %>

</div>
</body>
</html>
