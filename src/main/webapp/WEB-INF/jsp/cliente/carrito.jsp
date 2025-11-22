<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Carrito</title>

    <!-- CSS -->
    <link rel="preload" href="${ctx}/css/index.css" as="style">
    <link rel="stylesheet" href="${ctx}/css/index.css">

    <!-- Fuentes e Iconos -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
</head>

<body class="light-mode">
<div class="page-container">

    <jsp:include page="../fragments/clienteheader.jsp" />

    <main class="main-content">
        <section class="container cart-layout section-padding">
            <h1 class="section-title">Mi Carrito de Compras</h1>

            <!-- Si el carrito está vacío -->
            <c:if test="${empty carrito}">
                <p class="empty-text">Tu carrito está vacío 😔</p>
                <a href="${ctx}/cliente/catalogo" class="secondary-link">
                    <i class="fa-solid fa-reply"></i> Ir al catálogo
                </a>
            </c:if>

            <!-- Si hay productos -->
            <c:if test="${not empty carrito}">
                <div class="cart-content-grid">

                    <!-- Lista de Productos -->
                    <div class="cart-product-list">

                        <c:forEach var="item" items="${carrito}">
                            <div class="cart-item">

                                <img src="<c:out value='${item.producto.imagenUrl}'/>"
                                     alt="<c:out value='${item.producto.nombre}'/>"
                                     class="cart-item-image">

                                <div class="cart-item-info">
                                    <h3 class="cart-item-title">
                                        <c:out value="${item.producto.nombre}"/>
                                    </h3>

                                    <p class="cart-item-category">
                                        Talla: <c:out value="${item.talla}"/>
                                    </p>

                                    <p class="cart-item-price">
                                        S/
                                        <fmt:formatNumber value="${item.precioUnitario}"
                                                          type="number"
                                                          minFractionDigits="2"/>
                                    </p>
                                </div>

                                <!-- Cantidad y Talla -->
                                <div class="cart-item-quantity">
                                    <form action="${ctx}/cliente/carrito/actualizar" method="post">

                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="detalleId" value="${item.detalleId}"/>

                                        <label for="qty-${item.id}">Cantidad:</label>
                                        <input type="number"
                                               id="qty-${item.id}"
                                               name="cantidad"
                                               value="${item.cantidad}"
                                               min="1"
                                               class="quantity-input"
                                               aria-label="Cantidad del producto ${item.producto.nombre}"/>

                                        <label for="talla-${item.id}">Talla:</label>
                                        <select id="talla-${item.id}"
                                                name="talla"
                                                class="size-input"
                                                aria-label="Seleccionar talla para ${item.producto.nombre}">

                                            <c:forEach var="t" begin="35" end="45">
                                                <option value="${t}"
                                                    <c:if test="${t == item.talla}">selected</c:if> >
                                                    ${t}
                                                </option>
                                            </c:forEach>
                                        </select>

                                        <button type="submit" class="update-button">
                                            <i class="fa-solid fa-rotate"></i> Actualizar
                                        </button>
                                    </form>
                                </div>

                                <!-- Subtotal -->
                                <div class="cart-item-subtotal">
                                    <p>Subtotal</p>
                                    <p>
                                        S/
                                        <fmt:formatNumber value="${item.subtotal}"
                                                          type="number"
                                                          minFractionDigits="2"/>
                                    </p>
                                </div>

                                <!-- Botón Eliminar -->
                                <div class="cart-item-actions">
                                    <form action="${ctx}/cliente/carrito/eliminar" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="detalleId" value="${item.detalleId}"/>

                                        <button type="submit"
                                                class="delete-button"
                                                aria-label="Eliminar producto ${item.producto.nombre}">
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
                            <span>
                                S/
                                <fmt:formatNumber value="${totalPagar}"
                                                  type="number"
                                                  minFractionDigits="2"/>
                            </span>
                        </div>

                        <div class="summary-line">
                            <span>Costo de Envío</span>
                            <span class="free-shipping">¡Gratis!</span>
                        </div>

                        <div class="summary-total">
                            <span class="total-label">Total a Pagar</span>
                            <span class="total-value">
                                S/
                                <fmt:formatNumber value="${totalPagar}"
                                                  type="number"
                                                  minFractionDigits="2"/>
                            </span>
                        </div>

                        <form action="${ctx}/cliente/checkout" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button class="primary-button checkout-button">
                                <i class="fa-solid fa-lock"></i> Proceder al Pago
                            </button>
                        </form>

                        <a href="${ctx}/cliente/catalogo" class="secondary-link">
                            <i class="fa-solid fa-reply"></i> Seguir comprando
                        </a>

                    </div>
                </div>
            </c:if>

        </section>
    </main>

    <jsp:include page="../fragments/footer.jsp" />

</div>
</body>
</html>
