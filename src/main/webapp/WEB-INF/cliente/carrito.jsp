<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Carrito | ZapaStore</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="preload" href="${ctx}/css/index.css" as="style">
    <link rel="stylesheet" href="${ctx}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
</head>

<body class="light-mode">
<div class="page-container">

    <jsp:include page="../fragments/clienteheader.jsp" />

    <main class="main-content">
        <section class="container cart-layout section-padding">
            <h1 class="section-title">Mi Carrito de Compras</h1>

            <c:choose>
                <c:when test="${empty carrito}">
                    <div class="empty-cart">
                        <p class="empty-text">Tu carrito está vacío 😔</p>
                        <a href="${ctx}/cliente/catalogo" class="secondary-link">
                            <i class="fa-solid fa-reply" aria-hidden="true"></i> Ir al catálogo
                        </a>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="cart-content-grid">

                        <div class="cart-product-list">
                            <c:forEach var="item" items="${carrito}">
                                <article class="cart-item">

                                    <img src="<c:out value='${item.producto.imagenUrl}'/>"
                                         alt="<c:out value='${item.producto.nombre}'/>"
                                         class="cart-item-image"
                                         loading="lazy">

                                    <div class="cart-item-info">
                                        <h2 class="cart-item-title">
                                            <c:out value="${item.producto.nombre}"/>
                                        </h2>

                                        <p class="cart-item-category">
                                            Talla: <strong><c:out value="${item.talla}"/></strong>
                                        </p>

                                        <p class="cart-item-price">
                                            S/ <fmt:formatNumber value="${item.precioUnitario}" minFractionDigits="2"/>
                                        </p>
                                    </div>

                                    <div class="cart-item-quantity">
                                        <form action="${ctx}/cliente/carrito/actualizar" method="post">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="hidden" name="detalleId" value="${item.detalleId}"/>

                                            <label for="qty-${item.detalleId}">Cantidad:</label>
                                            <input type="number"
                                                   id="qty-${item.detalleId}"
                                                   name="cantidad"
                                                   value="${item.cantidad}"
                                                   min="1"
                                                   max="99"
                                                   class="quantity-input"
                                                   required
                                                   aria-label="Cantidad del producto <c:out value='${item.producto.nombre}'/>"/>

                                            <label for="talla-${item.detalleId}">Talla:</label>
                                            <select id="talla-${item.detalleId}"
                                                    name="talla"
                                                    class="size-input"
                                                    required
                                                    aria-label="Seleccionar talla para <c:out value='${item.producto.nombre}'/>">
                                                <c:forEach var="t" begin="35" end="45">
                                                    <option value="${t}" ${t == item.talla ? 'selected' : ''}>
                                                            ${t}
                                                    </option>
                                                </c:forEach>
                                            </select>

                                            <button type="submit" class="update-button" aria-label="Actualizar producto">
                                                <i class="fa-solid fa-rotate" aria-hidden="true"></i> Actualizar
                                            </button>
                                        </form>
                                    </div>

                                    <div class="cart-item-subtotal">
                                        <p>Subtotal</p>
                                        <p class="subtotal-value">
                                            S/ <fmt:formatNumber value="${item.subtotal}" minFractionDigits="2"/>
                                        </p>
                                    </div>

                                    <div class="cart-item-actions">
                                        <form action="${ctx}/cliente/carrito/eliminar" method="post">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="hidden" name="detalleId" value="${item.detalleId}"/>

                                            <button type="submit"
                                                    class="delete-button"
                                                    aria-label="Eliminar <c:out value='${item.producto.nombre}'/> del carrito">
                                                <i class="fa-solid fa-trash" aria-hidden="true"></i>
                                            </button>
                                        </form>
                                    </div>

                                </article>
                            </c:forEach>
                        </div>

                        <aside class="cart-summary-box card">
                            <h2 class="summary-heading">Resumen del Pedido</h2>

                            <dl class="summary-details">
                                <div class="summary-line">
                                    <dt>Productos (<c:out value="${totalItems}"/>)</dt>
                                    <dd>S/ <fmt:formatNumber value="${totalPagar}" minFractionDigits="2"/></dd>
                                </div>

                                <div class="summary-line">
                                    <dt>Costo de Envío</dt>
                                    <dd><span class="free-shipping">¡Gratis!</span></dd>
                                </div>

                                <div class="summary-total">
                                    <dt class="total-label">Total a Pagar</dt>
                                    <dd class="total-value">
                                        S/ <fmt:formatNumber value="${totalPagar}" minFractionDigits="2"/>
                                    </dd>
                                </div>
                            </dl>

                            <form action="${ctx}/cliente/checkout" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="primary-button checkout-button">
                                    <i class="fa-solid fa-lock" aria-hidden="true"></i> Proceder al Pago
                                </button>
                            </form>

                            <a href="${ctx}/cliente/catalogo" class="secondary-link">
                                <i class="fa-solid fa-reply" aria-hidden="true"></i> Seguir comprando
                            </a>
                        </aside>

                    </div>
                </c:otherwise>
            </c:choose>

        </section>
    </main>

    <jsp:include page="../fragments/footer.jsp" />

</div>
</body>
</html>