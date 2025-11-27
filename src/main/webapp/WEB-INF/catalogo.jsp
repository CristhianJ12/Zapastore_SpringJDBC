<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Catálogo</title>

    <!-- Estilos principales -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">

    <!-- Iconos y fuentes -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap">
</head>

<body class="light-mode">
<div class="page-container">

    <%@ include file="fragments/userheader.jsp" %>

    <main class="container main-content-padding">

        <div class="catalog-container">

            <!-- Encabezado + filtros -->
            <section class="catalog-toolbar section-padding-y">
                <div>
                    <h1 class="section-title">Catálogo de Productos</h1>
                    <p class="section-subtitle">Explora nuestras categorías y encuentra lo que necesitas.</p>
                </div>

                <div class="toolbar-actions">
                    <form method="get" action="${pageContext.request.contextPath}/catalogo">
                        <select class="select-category" name="categoriaId" onchange="this.form.submit()">
                            <option value="">Todas las categorías</option>

                            <c:forEach var="categoria" items="${categorias}">
                                <option value="${categoria.id}"
                                        <c:if test="${param.categoriaId == categoria.id}">selected</c:if>>
                                    <c:out value="${categoria.nombre}"/>
                                </option>
                            </c:forEach>

                        </select>
                    </form>
                </div>

            </section>

            <!-- GRID DE PRODUCTOS -->
            <section class="product-grid">

                <c:forEach var="producto" items="${productos}">
                    <article class="product-card">

                        <!-- Imagen -->
                        <div class="product-image-wrapper">
                            <img 
                                alt="<c:out value='${producto.nombre}'/>"
                                class="product-image"
                                src="${pageContext.request.contextPath}/${producto.imagenUrl}">
                        </div>

                        <!-- Contenido del producto -->
                        <div class="product-body">

                            <h3 class="product-title">
                                <c:out value="${producto.nombre}"/>
                            </h3>

                            <div class="product-description">
                                Estilo: <c:out value="${producto.categoriaNombre}"/>
                            </div>

                            <p class="product-description">
                                <c:out value="${producto.descripcion}"/>
                            </p>

                            <p class="product-price">
                                Precio: S/. <c:out value="${producto.precio}"/>
                            </p>

                            <button type="button"
                                    class="primary-button"
                                    onclick="window.location.href='${pageContext.request.contextPath}/login'">
                                Comprar
                            </button>

                        </div>

                    </article>
                </c:forEach>

            </section>

        </div>

    </main>

    <%@ include file="fragments/footer.jsp" %>

</div>
</body>
</html>
