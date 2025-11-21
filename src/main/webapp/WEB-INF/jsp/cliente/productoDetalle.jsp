<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | ${producto.nombre}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&display=swap">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    
</head>
<body class="light-mode">
<div class="page-container">

    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container product-detail-layout section-padding">

            <div class="product-image-container">
                <img src="${pageContext.request.contextPath}/${producto.imagenUrl}"
                     alt="${producto.nombre}" 
                     class="detail-product-image">
            </div>

            <div class="product-info-container">
                <p class="product-detail-category">Categoría: ${producto.categoriaNombre}</p>
                <h1 class="product-detail-title">${producto.nombre}</h1>

                <div class="product-price-section">
                    <span class="product-detail-price">S/ ${producto.precio}</span>
                    <span class="product-detail-stock">
                        <i class="fa-solid fa-circle-check"></i> ¡En stock!
                    </span>
                </div>

                <p class="product-detail-description">${producto.descripcion}</p>

                <!-- Formulario para agregar al carrito -->
                <form method="post" action="${pageContext.request.contextPath}/carrito/agregar" class="purchase-options">

                    <input type="hidden" name="productoId" value="${producto.id}">

                    <div class="option-group">
                        <label for="size-select">
                            <i class="fa-solid fa-shoe-prints"></i> Selecciona tu talla
                        </label>
                        <select name="talla" id="size-select" class="detail-select" required>
                            <option value="" disabled selected>Elige una talla</option>
                            <c:forEach var="talla" items="${producto.tallas}">
                                <option value="${talla}">${talla}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="option-group">
                        <label for="quantity-input">
                            <i class="fa-solid fa-hashtag"></i> Cantidad
                        </label>
                        <input type="number" name="cantidad" id="quantity-input" class="detail-input-number" value="1" min="1" max="10" required>
                    </div>

                    <button type="submit" class="primary-button add-to-cart-button">
                        <i class="fa-solid fa-cart-shopping"></i> Añadir al Carrito
                    </button>
                </form>

            </div>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>

</div>
</body>
</html>