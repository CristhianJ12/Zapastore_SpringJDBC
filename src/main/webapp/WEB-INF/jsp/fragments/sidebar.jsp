<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<aside class="sidebar">
    <div class="sidebar-header">
        <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo ZapaStore" class="logo-icon">
        <h1 class="logo-title">ZapaStore Admin</h1>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/metricas"
           class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/dashboard') ? 'is-active' : ''}">
            <span class="material-symbols-outlined">bar_chart</span>Métricas
        </a>

        <a href="${pageContext.request.contextPath}/admin/categorias"
           class="nav-item ${fn:contains(pageContext.request.requestURI, '/categorias') ? 'is-active' : ''}">
            <span class="material-symbols-outlined">shopping_bag</span>Categorías
        </a>

        <a href="${pageContext.request.contextPath}/admin/productos"
           class="nav-item ${fn:contains(pageContext.request.requestURI, '/productos') ? 'is-active' : ''}">
            <span class="material-symbols-outlined">store</span>Productos
        </a>

        <a href="${pageContext.request.contextPath}/admin/usuarios"
           class="nav-item ${fn:contains(pageContext.request.requestURI, '/usuarios') ? 'is-active' : ''}">
            <span class="material-symbols-outlined">group</span>Usuarios
        </a>
    </nav>

    <div class="sidebar-profile">
        <div class="profile-info">
            <div class="profile-avatar"
                 style='background-image: url("${pageContext.request.contextPath}/img/user.png");'>
            </div>
            <div>
                <p class="profile-name">Admin User</p>
                <p class="profile-email">admin@zapastore.com</p>
            </div>
        </div>
    </div>
</aside>
