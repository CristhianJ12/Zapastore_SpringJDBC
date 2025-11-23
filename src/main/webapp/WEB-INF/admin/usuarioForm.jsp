<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isEdit" value="${not empty usuario.idUsuario}" />
<c:set var="page" value="usuarios" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | <c:out value="${isEdit ? 'Editar Usuario' : 'Nuevo Usuario'}"/></title>

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
                        <span class="material-symbols-outlined icon-title">person_add</span>
                        <c:out value="${isEdit ? 'Editar Usuario' : 'Registrar Usuario'}"/>
                    </h2>
                    <a href="${pageContext.request.contextPath}/admin/usuarios" 
                        class="primary-button-admin">
                        <span class="material-symbols-outlined">arrow_back</span>
                        Volver a la Lista
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

                <section class="crud-area">
                    <form class="crud-form" 
                            action="${pageContext.request.contextPath}/admin/usuarios/${isEdit ? 'actualizar' : 'guardar'}" 
                            method="post">
                        
                        <c:if test="${isEdit}">
                            <input type="hidden" name="idUsuario" value="${usuario.idUsuario}"/>
                            <input type="hidden" name="estadoOriginal" value="${usuario.estado}"/>
                        </c:if>
                        <c:if test="${!isEdit}">
                            <input type="hidden" name="estado" value="Activo"/>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo">
                                <label for="nombre">Nombre</label>
                                <input type="text" id="nombre" name="nombre" required value="${usuario.nombre}">
                            </div>

                            <div class="campo">
                                <label for="correo">Correo electrónico</label>
                                <input type="email" id="correo" name="correo" required 
                                        value="${usuario.correo}" 
                                        ${isEdit ? 'readonly' : ''} 
                                        title="${isEdit ? 'El correo no puede ser modificado' : ''}">
                            </div>

                            <div class="campo">
                                <label for="telefono">Teléfono</label>
                                <input type="text" id="telefono" name="telefono" maxlength="20" value="${usuario.telefono}">
                            </div>

                            <div class="campo">
                                <label for="rol">Rol</label>
                                <select id="rol" name="rol" required>
                                    <option value="">Seleccionar rol</option>
                                    <option value="admin" ${usuario.rol == 'admin' ? 'selected' : ''}>Administrador</option>
                                    <option value="cliente" ${usuario.rol == 'cliente' ? 'selected' : ''}>Cliente</option>
                                </select>
                            </div>
                            
                            <c:if test="${isEdit}">
                                <div class="campo">
                                    <label for="estado">Estado</label>
                                    <select id="estado" name="estado" required>
                                        <option value="Activo" ${usuario.estado == 'Activo' ? 'selected' : ''}>Activo</option>
                                        <option value="Inactivo" ${usuario.estado == 'Inactivo' ? 'selected' : ''}>Inactivo</option>
                                    </select>
                                </div>
                            </c:if>
                            
                            <div class="campo">
                                <label for="contrasena">Contraseña</label>
                                <input type="password" id="contrasena" name="contrasena"
                                        placeholder="${isEdit ? 'Dejar en blanco para mantener la actual' : 'Requerida'}"
                                        <c:if test="${!isEdit}">required</c:if>> <%-- Contraseña requerida solo al crear --%>
                            </div>
                            <c:if test="${!isEdit}">
                                <div class="campo">
                                    <label for="confirmContrasena">Confirmar Contraseña</label>
                                    <input type="password" id="confirmContrasena" name="confirmContrasena" required>
                                </div>
                            </c:if>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="primary-button-admin">
                                <span class="material-symbols-outlined">save</span>
                                <c:out value="${isEdit ? 'Actualizar Usuario' : 'Guardar Usuario'}"/>
                            </button>
                        </div>
                    </form>
                </section>
            </div>
        </main>
    </div>
</body>
</html>