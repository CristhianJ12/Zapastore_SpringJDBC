<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="page" value="productos" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Lista de Productos</title>

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
                    <span class="material-symbols-outlined icon-title">inventory_2</span>
                    Lista de Productos
                </h2>

                <a href="${pageContext.request.contextPath}/admin/productos/crear" class="primary-button-admin">
                    <span class="material-symbols-outlined">add_circle</span>
                    Nuevo Producto
                </a>
            </div>

            <!-- Mensajes -->
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

            <!-- Filtro y búsqueda -->
            <form action="${pageContext.request.contextPath}/admin/productos/lista" method="GET" class="search-bar">

                <input type="text" name="q" placeholder="Buscar..."
                       class="input-search"
                       value="${currentQuery != null ? currentQuery : ''}">

                <label style="margin-left: 18px;">
                    <input type="checkbox" name="showInactive"
                           onchange="this.form.submit()"
                           <c:if test="${showInactive}">checked</c:if> >
                    Mostrar inactivos
                </label>

                <button type="submit" class="icon-button search-button">
                    <span class="material-symbols-outlined">search</span>
                </button>

                <c:if test="${not empty currentQuery}">
                    <a href="${pageContext.request.contextPath}/admin/productos/lista" class="icon-button clear-search">
                        <span class="material-symbols-outlined">close</span>
                    </a>
                </c:if>

            </form>

            <!-- Tabla -->
            <section class="crud-lista">
                <table class="crud-table">
                    <thead>
                    <tr>
                        <th>Imagen</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="producto" items="${productos}">
                        <tr class="${producto.estado == 'Inactivo' ? 'fila-inactiva' : ''}">
                            <td>
                                <img src="${pageContext.request.contextPath}/${producto.imagenUrl}" class="tabla-img">
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

                            <td>
                                <c:choose>
                                    <c:when test="${producto.estado == 'Activo'}">
                                        <span class="badge-success">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-danger">Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="actions-cell">
                                <a class="icon-button edit"
                                   href="${pageContext.request.contextPath}/admin/productos/editar/${producto.id}">
                                    <span class="material-symbols-outlined">edit</span>
                                </a>

                                <c:choose>
                                    <c:when test="${producto.estado == 'Activo'}">
                                        <a class="icon-button delete"
                                           href="${pageContext.request.contextPath}/admin/productos/eliminar/${producto.id}"
                                           onclick="return confirm('¿Desactivar ${producto.nombre}?');">
                                            <span class="material-symbols-outlined">delete</span>
                                        </a>
                                    </c:when>

                                    <c:otherwise>
                                        <a class="icon-button restore"
                                           href="${pageContext.request.contextPath}/admin/productos/activar/${producto.id}"
                                           onclick="return confirm('¿Reactivar ${producto.nombre}?');">
                                            <span class="material-symbols-outlined">restart_alt</span>
                                        </a>
                                    </c:otherwise>
                                </c:choose>

                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty productos}">
                        <tr>
                            <td colspan="7" class="tabla-vacia">

                                <c:if test="${not empty currentQuery}">
                                    No se encontraron productos para "${currentQuery}".
                                </c:if>

                                <c:if test="${empty currentQuery}">
                                    No hay productos registrados.
                                </c:if>

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
