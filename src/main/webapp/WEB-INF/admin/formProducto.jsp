<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Definición de la variable isEdit para toda la lógica del formulario --%>
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
                    <span class="material-symbols-outlined">arrow_back</span>Volver a la lista
                </a>
            </div>

            <section class="crud-area">
                <form:form class="crud-form" method="POST"
                           action="${pageContext.request.contextPath}/admin/productos/guardar"
                           modelAttribute="producto">

                    <%-- Corrección 1: Usamos 'isEdit' y 'id' --%>
                    <c:if test="${isEdit}"> 
                        <form:hidden path="id"/>
                    </c:if>

                    <div class="form-grid">
                        <div class="campo">
                            <label for="nombre">Nombre del producto</label>
                            <form:input path="nombre" id="nombre" placeholder="Ej. Nike Air Zoom" required="true"/>
                        </div>

                        <%-- Selección de Categoría --%>
                        <div class="campo categoria-select">
                            <label for="categoriaID">Categoría</label>
                            <div class="categoria-flex">
                                <%-- Corrección 2: Usamos 'categoriaID' --%>
                                <form:select path="categoriaID" id="categoriaID" required="true" class="select-control">
                                    <form:option value="" label="Seleccione una Categoría"/>
                                    <c:forEach var="cat" items="${categorias}">
                                        <form:option value="${cat.id}" label="${cat.nombre}"/>
                                    </c:forEach>
                                </form:select>
                                <a href="${pageContext.request.contextPath}/admin/categorias"
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
                            <label for="imagenUrl">Imagen del producto (URL)</label>
                            <%-- Corrección 3: Usamos 'imagenUrl' --%>
                            <form:input path="imagenUrl" type="url" id="imagenUrl" placeholder="https://..." required="true"/>
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