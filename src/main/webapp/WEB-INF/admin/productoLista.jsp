<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="page" value="productos" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Lista de Productos</title>

    <!-- Fuentes y estilos -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body class="admin-body">
<div class="admin-layout">

    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/jsp/fragments/sidebar.jsp" />

    <main class="main-panel">
        <!-- Header -->
        <jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />

        <!-- Contenido principal -->
        <div class="content-wrapper">
            <div class="page-header">
                <h2 class="page-title">
                    <span class="material-symbols-outlined icon-title">inventory_2</span>
                    Lista de Productos
                </h2>
                <a href="${pageContext.request.contextPath}/admin/productos/crear" class="primary-button-admin">
                    <span class="material-symbols-outlined">add_circle</span>
                    Nuevo Producto
                </a>
            </div>

            <!-- Mensajes de alerta -->
            <c:if test="${not empty mensaje}">
                <div class="alert success">
                    <span class="material-symbols-outlined">check_circle</span>
                    ${mensaje}
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">
                    <span class="material-symbols-outlined">error</span>
                    ${error}
                </div>
            </c:if>

            <!-- Barra de búsqueda -->
            <form action="${pageContext.request.contextPath}/admin/productos/lista" method="GET" class="search-bar">
                <input type="text" name="q" placeholder="Buscar producto por nombre..." class="input-search"
                       value="${currentQuery != null ? currentQuery : ''}">
                <button type="submit" class="icon-button search-button">
                    <span class="material-symbols-outlined">search</span>
                </button>
                <c:if test="${not empty currentQuery}">
                    <a href="${pageContext.request.contextPath}/admin/productos/lista" class="icon-button clear-search" title="Limpiar búsqueda">
                        <span class="material-symbols-outlined">close</span>
                    </a>
                </c:if>
            </form>

            <!-- Tabla de productos -->
            <section class="crud-lista">
                <table class="crud-table">
                    <thead>
                    <tr>
                        <th>Imagen</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Descripción</th>
                        <th>Precio (S/)</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="producto" items="${productos}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" 
                                     alt="${producto.nombre}" class="tabla-img">
                            </td>
                            <td>${producto.nombre}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty producto.categoriaNombre}">
                                        ${producto.categoriaNombre}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">ID: ${producto.categoriaID}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.precio}</td>
                            <td class="actions-cell">
                                <a href="${pageContext.request.contextPath}/admin/productos/editar/${producto.id}"
                                   class="icon-button edit" title="Editar">
                                    <span class="material-symbols-outlined">edit</span>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/productos/eliminar/${producto.id}"
                                   class="icon-button delete" title="Eliminar"
                                   onclick="return confirm('¿Seguro que quieres desactivar ${producto.nombre}?');">
                                    <span class="material-symbols-outlined">delete</span>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty productos}">
                        <tr>
                            <td colspan="6" class="tabla-vacia">
                                <c:choose>
                                    <c:when test="${not empty currentQuery}">
                                        No se encontraron productos para la búsqueda "${currentQuery}".
                                    </c:when>
                                    <c:otherwise>
                                        No hay productos registrados en la base de datos.
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </main>
</div>
</body>
</html>
