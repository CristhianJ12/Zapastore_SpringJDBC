<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="page" value="categorias" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ZapaStore | Administrar Categorías</title>

    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>" />
</head>

<body class="admin-body">
<div class="admin-layout">

    <jsp:include page="/WEB-INF/fragments/sidebar.jsp" />

    <main class="main-panel">

        <jsp:include page="/WEB-INF/fragments/header.jsp" />

        <div class="content-wrapper">

            <div class="page-header">
                <h2 class="page-title">
                    <span class="material-symbols-outlined icon-title">category</span>
                    Administrar Categorías
                </h2>
            </div>

            <c:if test="${not empty msg}">
                <div class="alert success">
                    <c:out value="${msg}" />
                </div>
            </c:if>

            <section class="crud-area">

                <form class="crud-form" action="<c:url value='/admin/categorias/guardar'/>" method="POST">
                    <div class="form-grid">

                        <div class="campo">
                            <label for="nombre">Nueva categoría</label>
                            <input type="text"
                                   id="nombre"
                                   name="nombre"
                                   placeholder="Ej. Deportivos"
                                   required
                                   class="input-text" />
                        </div>

                        <div class="campo">
                            <label for="estado">Estado</label>
                            <select id="estado" name="estado" class="input-select">
                                <option value="Activo" selected>Activo</option>
                                <option value="Inactivo">Inactivo</option>
                            </select>
                        </div>

                        <div class="campo form-actions">
                            <button type="submit" class="primary-button-admin small-btn">
                                <span class="material-symbols-outlined">add</span> Agregar
                            </button>
                        </div>

                    </div>
                </form>

                <div class="crud-lista">
                    <table class="crud-table" role="table" aria-label="Lista de categorías">
                        <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Nombre de la Categoría</th>
                            <th scope="col">Estado</th>
                            <th scope="col">Acciones</th>
                        </tr>
                        </thead>

                        <tbody>

                        <c:forEach var="cat" items="${categorias}">
                            <c:set var="chipClass" value="${cat.estado == 'Activo' ? 'chip-activo' : 'chip-inactivo'}" />
                            <tr>
                                <td><c:out value="${cat.id}" /></td>
                                <td><c:out value="${cat.nombre}" /></td>

                                <td>
                                    <span class="chip ${chipClass}">
                                        <c:out value="${cat.estado}" />
                                    </span>
                                </td>

                                <td class="actions-cell">

                                    <a href="<c:url value='/admin/categorias/mostrarEditar'>
                                                    <c:param name='id' value='${cat.id}'/>
                                                </c:url>"
                                       class="icon-button edit"
                                       title="Editar categoría">
                                        <span class="material-symbols-outlined">edit</span>
                                    </a>

                                    <c:if test="${cat.estado == 'Activo'}">
                                        <a href="<c:url value='/admin/categorias/eliminar'>
                                                        <c:param name='id' value='${cat.id}'/>
                                                   </c:url>"
                                           class="icon-button delete"
                                           title="Inactivar categoría"
                                           onclick="return confirm('¿Está seguro de desactivar la categoría &quot;${fn:escapeXml(cat.nombre)}&quot;?');">
                                            <span class="material-symbols-outlined">delete</span>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty categorias}">
                            <tr>
                                <td colspan="4" class="tabla-vacia">
                                    No hay categorías registradas aún.
                                </td>
                            </tr>
                        </c:if>

                        </tbody>
                    </table>
                </div>

            </section>
        </div>
    </main>
</div>
</body>
</html>
