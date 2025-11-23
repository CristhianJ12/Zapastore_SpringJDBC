<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%-- 💡 IMPORTANTE: Importación de JSTL Functions --%>

<c:set var="page" value="usuarios" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Lista de Usuarios</title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body class="admin-body">
    <div class="admin-layout">
        <jsp:include page="/WEB-INF/fragments/sidebar.jsp" />

        <main class="main-panel">
            <jsp:include page="/WEB-INF/fragments/header.jsp" />

            <div class="content-wrapper">
                <div class="page-header">
                    <h2 class="page-title">
                        <span class="material-symbols-outlined icon-title">group</span>
                        Lista de Usuarios
                    </h2>
                    <a href="${pageContext.request.contextPath}/admin/usuarios/mostrarCrear" class="primary-button-admin"> 
                        <span class="material-symbols-outlined">add_circle</span>
                        Nuevo Usuario
                    </a>
                </div>

                <c:if test="${not empty msg}">
                    <div class="alert success">
                        <span class="material-symbols-outlined">check_circle</span>
                        ${msg}
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert error">
                        <span class="material-symbols-outlined">error</span>
                        ${error}
                    </div>
                </c:if>

                <section class="crud-lista">
                    <table class="crud-table">
                        <thead>
                            <tr>
                                <th>ID (UUID)</th>
                                <th>Nombre</th>
                                <th>Correo</th>
                                <th>Teléfono</th>
                                <th>Rol</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="usuario" items="${usuarios}">
                                <tr>
                                    <td>
                                        <%-- ✅ Lógica corregida para prevenir StringIndexOutOfBoundsException --%>
                                        <c:choose>
                                            <c:when test="${fn:length(usuario.idUsuario) >= 8}">
                                                ${usuario.idUsuario.substring(0, 8)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${usuario.idUsuario}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${usuario.nombre}</td>
                                    <td>${usuario.correo}</td>
                                    <td>${usuario.telefono}</td>
                                    <td>
                                        <span class="chip chip-rol ${usuario.rol == 'admin' ? 'chip-admin' : 'chip-cliente'}">
                                            ${usuario.rol}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="chip chip-estado ${usuario.activo ? 'chip-activo' : 'chip-inactivo'}">
                                            ${usuario.estado}
                                        </span>
                                    </td>
                                    <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/admin/usuarios/mostrarEditar/${usuario.idUsuario}" 
                                           class="icon-button edit" title="Editar">
                                            <span class="material-symbols-outlined">edit</span>
                                        </a>

                                        <c:choose>
                                            <c:when test="${usuario.activo}">
                                                <a href="${pageContext.request.contextPath}/admin/usuarios/desactivar/${usuario.idUsuario}" 
                                                   class="icon-button delete" title="Desactivar"
                                                   onclick="return confirm('¿Estás seguro de DESACTIVAR al usuario ${usuario.nombre}?');">
                                                    <span class="material-symbols-outlined">toggle_off</span>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/admin/usuarios/activar/${usuario.idUsuario}" 
                                                   class="icon-button success" title="Activar"
                                                   onclick="return confirm('¿Estás seguro de ACTIVAR al usuario ${usuario.nombre}?');">
                                                    <span class="material-symbols-outlined">toggle_on</span>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${empty usuarios}">
                        <p class="no-data-msg">
                            No hay usuarios registrados aún.
                        </p>
                    </c:if>
                </section>
            </div>
        </main>
    </div>
</body>
</html>