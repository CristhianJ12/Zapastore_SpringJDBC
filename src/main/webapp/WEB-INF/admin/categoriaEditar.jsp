<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="isEdit" value="${not empty categoria.id}" />

<c:choose>
    <c:when test="${isEdit}">
        <c:set var="formAction" value="/admin/categorias/actualizar" />
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="/admin/categorias/guardar" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>
        ZapaStore | <c:out value="${isEdit ? 'Editar Categoría' : 'Registrar Nueva Categoría'}" />
    </title>

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
                    <c:choose>
                        <c:when test="${isEdit}">
                            Editar Categoría: <c:out value="${categoria.nombre}" />
                        </c:when>
                        <c:otherwise>
                            Registrar Nueva Categoría
                        </c:otherwise>
                    </c:choose>
                </h2>

                <a href="<c:url value='/admin/categorias'/>"
                   class="primary-button-admin"
                   aria-label="Volver a la lista de categorías">
                    <span class="material-symbols-outlined">arrow_back</span>
                    Volver a Categorías
                </a>
            </div>

            <c:if test="${not empty msg}">
                <div class="alert danger">
                    <c:out value="${msg}" />
                </div>
            </c:if>

            <section class="crud-area">

                <form class="crud-form" method="POST" action="<c:url value='${formAction}'/>">

                    <c:if test="${isEdit}">
                        <input type="hidden" name="id" value="${categoria.id}" />
                    </c:if>

                    <div class="form-grid">

                        <div class="campo">
                            <label for="nombre">Nombre de la Categoría</label>
                            <input type="text"
                                   id="nombre"
                                   name="nombre"
                                   class="input-text"
                                   placeholder="Ej. Deportivos"
                                   required
                                   value="${fn:escapeXml(categoria.nombre)}" />
                        </div>

                        <div class="campo">
                            <label for="estado">Estado</label>
                            <select id="estado" name="estado" class="input-select" required>
                                <option value="Activo" <c:if test="${categoria.estado == 'Activo'}">selected</c:if>>Activo</option>
                                <option value="Inactivo" <c:if test="${categoria.estado == 'Inactivo'}">selected</c:if>>Inactivo</option>
                            </select>
                        </div>

                    </div>

                    <div class="form-actions">
                        <button type="submit" class="primary-button-admin">
                            <span class="material-symbols-outlined">save</span>
                            <c:choose>
                                <c:when test="${isEdit}">Guardar Cambios</c:when>
                                <c:otherwise>Registrar Categoría</c:otherwise>
                            </c:choose>
                        </button>
                    </div>

                </form>
            </section>

        </div>

    </main>
</div>
</body>
</html>
