<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="isEdit" value="${not empty producto.id}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | <c:out value="${isEdit ? 'Editar Producto' : 'Nuevo Producto'}"/></title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body class="admin-body">
<div class="admin-layout">

    <jsp:include page="/WEB-INF/fragments/sidebar.jsp"/>
    <main class="main-panel">

        <jsp:include page="/WEB-INF/fragments/header.jsp"/>

        <div class="content-wrapper">

            <div class="page-header">
                <h2 class="page-title">
                    <span class="material-symbols-outlined icon-title">inventory_2</span>
                    <c:out value="${isEdit ? 'Editar Producto' : 'Registrar Producto'}"/>
                </h2>

                <a href="${pageContext.request.contextPath}/admin/productos/lista"
                   class="primary-button-admin">
                    <span class="material-symbols-outlined">arrow_back</span>Volver
                </a>
            </div>

            <section class="crud-area">
                <form:form class="crud-form" method="POST"
                           action="${pageContext.request.contextPath}/admin/productos/guardar"
                           modelAttribute="producto">

                    <c:if test="${isEdit}">
                        <form:hidden path="id" />
                    </c:if>

                    <div class="form-grid">

                        <div class="campo">
                            <label for="nombre">Nombre</label>
                            <form:input id="nombre" path="nombre" required="true" />
                        </div>

                        <div class="campo categoria-select">
                            <label for="categoriaID">Categoría</label>

                            <form:select id="categoriaID" path="categoriaID" required="true" class="select-control">
                                <form:option value="" label="Seleccione una categoría" />
                                <c:forEach var="cat" items="${categorias}">
                                    <form:option value="${cat.id}" label="${cat.nombre}" />
                                </c:forEach>
                            </form:select>

                            <a href="${pageContext.request.contextPath}/admin/categorias"
                               class="secondary-button-admin small-btn">
                                <span class="material-symbols-outlined">tune</span> Administrar
                            </a>
                        </div>

                        <div class="campo">
                            <label for="precio">Precio</label>
                            <form:input id="precio" path="precio" type="number" min="0" step="0.01" required="true" />
                        </div>

                        <div class="campo campo-full">
                            <label for="imagenUrl">Imagen (URL)</label>
                            <form:input id="imagenUrl" path="imagenUrl" placeholder="https://..." required="true" />
                        </div>

                        <div class="campo campo-full">
                            <label for="descripcion">Descripción</label>
                            <form:textarea id="descripcion" path="descripcion" rows="4" />
                        </div>


                        <c:if test="${isEdit}">
                            <div class="campo">
                                <label for="estado">Estado</label>
                                <form:select id="estado" path="estado" required="true" class="select-control">
                                    <form:option value="Activo">Activo</form:option>
                                    <form:option value="Inactivo">Inactivo</form:option>
                                </form:select>
                            </div>
                        </c:if>

                    </div>

                    <div class="form-actions">
                        <button type="submit" class="primary-button-admin">
                            <span class="material-symbols-outlined">save</span>
                            <c:out value="${isEdit ? 'Actualizar' : 'Guardar'}"/>
                        </button>
                    </div>

                </form:form>
            </section>

        </div>
    </main>
</div>

</body>
</html>
