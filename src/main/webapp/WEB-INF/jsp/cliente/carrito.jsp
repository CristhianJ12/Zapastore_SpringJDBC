<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Carrito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"> 
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
</head>
<body class="light-mode">
<div class="page-container">

    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container cart-layout section-padding">
            <h1 class="section-title" style="margin-bottom: var(--space-8);">Mi Carrito de Compras</h1>

            <div class="cart-content-grid">
                <!-- Lista de Productos -->
                <div class="cart-product-list">
                    <c:forEach var="item" items="${carrito}">
                        <div class="cart-item">
                            <img src="${item.producto.imagen_url}" 
                                 alt="${item.producto.nombre}" class="cart-item-image">

                            <div class="cart-item-info">
                                <h3 class="cart-item-title">${item.producto.nombre}</h3>
                                <p class="cart-item-category">Talla: ${item.talla}</p>
                                <p class="cart-item-price">S/ ${item.precioUnitario}</p>
                            </div>

                            <div class="cart-item-quantity">
                                <form action="${pageContext.request.contextPath}/carrito/actualizar" method="post">
                                    <input type="hidden" name="detalleId" value="${item.id}">
                                    <label for="qty-${item.id}">Cantidad:</label>
                                    <input type="number" id="qty-${item.id}" name="cantidad" value="${item.cantidad}" min="1" class="quantity-input">

                                    <label for="talla-${item.id}">Talla:</label>
                                    <input type="number" id="talla-${item.id}" name="talla" value="${item.talla}" min="1" class="size-input">

                                    <button type="submit" class="update-button">Actualizar</button>
                                </form>
                            </div>

                            <div class="cart-item-subtotal">
                                <p>Subtotal</p>
                                <p>S/ ${item.subtotal}</p>
                            </div>

                            <div class="cart-item-actions">
                                <form action="${pageContext.request.contextPath}/carrito/eliminar" method="post">
                                    <input type="hidden" name="detalleId" value="${item.id}">
                                    <button type="submit" class="delete-button" title="Eliminar Producto">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Resumen del Pedido -->
                <div class="cart-summary-box card">
                    <h3 class="summary-heading">Resumen del Pedido</h3>
                    <div class="summary-line">
                        <span>Productos (${totalItems})</span>
                        <span>S/ ${totalPagar}</span>
                    </div>
                    <div class="summary-line">
                        <span>Costo de Envío</span>
                        <span class="free-shipping">¡Gratis!</span>
                    </div>
                    <div class="summary-total">
                        <span class="total-label">Total a Pagar</span>
                        <span class="total-value">S/ ${totalPagar}</span>
                    </div>
                    <form action="${pageContext.request.contextPath}/checkout" method="post">
                        <button class="primary-button checkout-button">
                            <i class="fa-solid fa-lock"></i> Proceder al Pago
                        </button>
                    </form>
                    <a href="${pageContext.request.contextPath}/cliente/catalogo" class="secondary-link">
                        <i class="fa-solid fa-reply"></i> Seguir comprando
                    </a>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>
</div>
</body>
</html>
