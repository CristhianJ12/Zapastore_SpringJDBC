<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Iniciar Sesión</title>
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="light-mode">

<div class="page-container">

    <header class="main-header">
        <div class="container header-inner">
            <div class="header-left">
                <a class="logo" href="<c:url value='/'/>">
                    <img src="<c:url value='/resources/img/logo.png'/>" alt="Logo ZapaStore" class="logo-image" height="24">
                    <h1 class="logo-text">ZapaStore</h1>
                </a>
            </div>
            <div class="header-right">
                </div>
        </div>
    </header>

    <main class="main-content">
        <section class="container login-section">
            <div class="login-form-container">
                <h2 class="section-title login-title">Iniciar Sesión</h2>

                <c:if test="${not empty error}">
                    <div class="alert error">${error}</div>
                </c:if>
                <c:if test="${not empty msg}">
                    <div class="alert success">${msg}</div>
                </c:if>

                <c:if test="${param.logout != null}">
                    <div class="alert success">
                        Sesión cerrada correctamente.
                    </div>
                </c:if>

                <form action="<c:url value='/login'/>" method="post" class="login-form">
                    
                    <div class="form-group">
                        <label for="correo" class="form-label">Correo electrónico</label>
                        <input type="email" name="correo" id="correo" class="form-input" placeholder="ejemplo@correo.com" value="${correo}" required>
                    </div>

                    <div class="form-group">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <input type="password" name="contrasena" id="contrasena" class="form-input" placeholder="********" required>
                    </div>

                    <button type="submit" class="primary-button login-button">Ingresar</button>

                </form>

                <p class="signup-link">
                    ¿No tienes cuenta? 
                    <a href="<c:url value='/registrar'/>" class="color-primary-link">Regístrate</a>
                </p>
            </div>
        </section>
    </main>
</div>

</body>
</html>