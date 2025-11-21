<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<header class="main-header">
    <div class="container header-inner">
        <div class="header-left">
            <a class="logo" href="home">
                <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo ZapaStore" class="logo-image" height="24">
                <h1 class="logo-text">ZapaStore</h1>
            </a>
            <nav class="main-nav">
                <a href="${pageContext.request.contextPath}/catalogo">Catálogo</a>
                <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
                <a href="${pageContext.request.contextPath}/ofertas">Ofertas</a>
            </nav>
        </div>
        <div class="header-right">
            <a href="login">
                <button class="secondary-button">Iniciar Sesión</button>
            </a>
            <a href="home">
                <div class="profile-pic" style='background-image: url("${pageContext.request.contextPath}/img/user.png");' aria-label="Perfil de Usuario"></div>
            </a>
        </div>
    </div>
</header>