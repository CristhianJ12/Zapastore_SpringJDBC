<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<header class="main-header">
    <div class="container header-inner">
        <div class="header-left">
            <a class="logo" href="${pageContext.request.contextPath}/cliente/home">
                <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo ZapaStore" class="logo-image" height="24">
                <h1 class="logo-text">ZapaStore</h1>
            </a>
            <nav class="main-nav">
                <a href="${pageContext.request.contextPath}/cliente/catalogo">Catálogo</a>
                <a href="${pageContext.request.contextPath}/cliente/contacto">Contacto</a>
                <a href="${pageContext.request.contextPath}/cliente/ofertas">Ofertas</a>
            </nav>
        </div>
        <div class="header-right"> 

            <a href="${pageContext.request.contextPath}/cliente/carrito" class="cart-icon-link" aria-label="Carrito de Compras" style="color: var(--color-primary);">
                <i class="fa-solid fa-cart-shopping cart-icon"></i>
            </a>

            <!-- Avatar y Logout -->
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/cliente/perfil" class="icon-button" title="Perfil de Usuario">
                    <span class="material-symbols-outlined">person</span>
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="icon-button" title="Cerrar sesión">
                    <span class="material-symbols-outlined">logout</span>
                </a>
            </div>
        </div>
    </div>
</header>
