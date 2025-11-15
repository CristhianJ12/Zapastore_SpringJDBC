<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="isEdit" value="${not empty producto.producto_ID}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | <c:out value="${isEdit ? 'Editar Producto' : 'Nuevo Producto'}"/></title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
</head>

<body class="admin-body">
<div class="admin-layout">

    <jsp:include page="/WEB-INF/jsp/fragments/sidebar.jsp"/>

    <main class="main-panel">
        <jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>

        <div class="content-wrapper">
            <div class="page-header">
                <h2 class="page-title">
                    <span class="material-symbols-outlined icon-title">inventory_2</span>
                    <c:out value="${isEdit ? 'Editar Producto' : 'Registrar Producto'}"/>
                </h2>
                <a href="${pageContext.request.contextPath}/admin/productos/lista"
                   class="primary-button-admin">
                    <span class="material-symbols-outlined">arrow_back</span>Volver a la lista
                </a>
            </div>

            <section class="crud-area">
                <form:form class="crud-form" method="POST"
                           action="${pageContext.request.contextPath}/admin/productos/guardar"
                           modelAttribute="producto">

                    <c:if test="${isEdit}">
                        <form:hidden path="producto_ID"/>
                    </c:if>

                    <div class="form-grid">
                        <div class="campo">
                            <label for="nombre">Nombre del producto</label>
                            <form:input path="nombre" id="nombre" placeholder="Ej. Nike Air Zoom" required="true"/>
                        </div>

                        <!-- Selección de Categoría -->
                        <div class="campo categoria-select">
                            <label for="categoria_ID">Categoría</label>
                            <div class="categoria-flex">
                                <form:select path="categoria_ID" id="categoria_ID" required="true" class="select-control">
                                    <form:option value="" label="Seleccione una Categoría"/>
                                    <c:forEach var="cat" items="${categorias}">
                                        <form:option value="${cat.categoria_ID}" label="${cat.nombre}"/>
                                    </c:forEach>
                                </form:select>
                                <a href="${pageContext.request.contextPath}/admin/categorias/lista"
                                   class="secondary-button-admin small-btn">
                                    <span class="material-symbols-outlined">tune</span>
                                    Administrar categorías
                                </a>
                            </div>
                        </div>

                        <div class="campo">
                            <label for="precio">Precio (S/)</label>
                            <form:input path="precio" type="number" id="precio" placeholder="Ej. 350" min="0" step="0.01" required="true"/>
                        </div>

                        <div class="campo campo-full">
                            <label for="img_Url">Imagen del producto (URL)</label>
                            <form:input path="img_Url" type="url" id="img_Url" placeholder="https://..." required="true"/>
                        </div>

                        <div class="campo campo-full">
                            <label for="descripcion">Descripción</label>
                            <form:textarea path="descripcion" id="descripcion" rows="4" placeholder="Describe el producto..."/>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="primary-button-admin">
                            <span class="material-symbols-outlined">save</span>
                            <c:out value="${isEdit ? 'Actualizar Producto' : 'Guardar Producto'}"/>
                        </button>
                    </div>
                </form:form>
            </section>
        </div>
    </main>
</div>
</body>
</html>
