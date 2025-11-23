<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="page" value="metricas" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="meses" value="${['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Métricas de Ventas</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body class="admin-body">
<div class="admin-layout">
    <jsp:include page="/WEB-INF/fragments/sidebar.jsp"/>
    <main class="main-panel">
        <jsp:include page="/WEB-INF/fragments/header.jsp"/>

        <div class="content-wrapper">
            <div class="page-header">
                <h1 class="page-title">
                    <span class="material-symbols-outlined icon-title">analytics</span>
                    Métricas de Ventas
                </h1>
            </div>

            <c:if test="${not empty mensaje}">
                <div class="alert success" role="alert">
                    <c:out value="${mensaje}" />
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error" role="alert">
                    <c:out value="${error}" />
                </div>
            </c:if>

            <section class="metrics-area">
                <div class="metrics-grid">

                    <article class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Día</h2>
                            <form action="${ctx}/admin/metricas" method="get" class="panel-controls">
                                <label for="dia">Día:</label>
                                <input type="date" id="dia" name="dia" value="${diaSeleccionado}" required />
                                <input type="hidden" name="semana" value="${fechaSemana}" />
                                <input type="hidden" name="mesInicio" value="${mesInicio}" />
                                <input type="hidden" name="mesFin" value="${mesFin}" />
                                <input type="hidden" name="anio" value="${anio}" />
                                <button type="submit" class="icon-button" title="Filtrar" aria-label="Filtrar ventas por día">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoDia" aria-label="Gráfico de ventas por día"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total día</span>
                                <span class="total-value">S/ <fmt:formatNumber value="${totalDia != null ? totalDia : 0}" minFractionDigits="2" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosDia != null ? pedidosDia : 0}" /></span>
                            </div>
                        </div>
                    </article>

                    <article class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Semana</h2>
                            <form action="${ctx}/admin/metricas" method="get" class="panel-controls">
                                <label for="semana">Semana:</label>
                                <input type="date" id="semana" name="semana" value="${fechaSemana}" required />
                                <input type="hidden" name="dia" value="${diaSeleccionado}" />
                                <input type="hidden" name="mesInicio" value="${mesInicio}" />
                                <input type="hidden" name="mesFin" value="${mesFin}" />
                                <input type="hidden" name="anio" value="${anio}" />
                                <button type="submit" class="icon-button" title="Filtrar" aria-label="Filtrar ventas por semana">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoSemana" aria-label="Gráfico de ventas por semana"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total semana</span>
                                <span class="total-value">S/ <fmt:formatNumber value="${totalSemana != null ? totalSemana : 0}" minFractionDigits="2" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosSemana != null ? pedidosSemana : 0}" /></span>
                            </div>
                        </div>
                    </article>

                    <article class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Mes</h2>
                            <form action="${ctx}/admin/metricas" method="get" class="panel-controls">
                                <label for="mesInicio">Desde:</label>
                                <select id="mesInicio" name="mesInicio" required>
                                    <c:forEach var="m" items="${meses}" varStatus="status">
                                        <option value="${status.index + 1}" ${mesInicio == status.index + 1 ? 'selected' : ''}>
                                            <c:out value="${m}" />
                                        </option>
                                    </c:forEach>
                                </select>

                                <label for="mesFin">Hasta:</label>
                                <select id="mesFin" name="mesFin" required>
                                    <c:forEach var="m" items="${meses}" varStatus="status">
                                        <option value="${status.index + 1}" ${mesFin == status.index + 1 ? 'selected' : ''}>
                                            <c:out value="${m}" />
                                        </option>
                                    </c:forEach>
                                </select>

                                <label for="anio" class="sr-only">Año</label>
                                <input type="number" id="anio" name="anio" value="${anio}" placeholder="Año" min="2020" max="2100" style="min-width: 80px;" required />
                                <input type="hidden" name="dia" value="${diaSeleccionado}" />
                                <input type="hidden" name="semana" value="${fechaSemana}" />
                                <button type="submit" class="icon-button" title="Filtrar" aria-label="Filtrar ventas por mes">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoMes" aria-label="Gráfico de ventas por mes"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total mes</span>
                                <span class="total-value">S/ <fmt:formatNumber value="${totalMes != null ? totalMes : 0}" minFractionDigits="2" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosMes != null ? pedidosMes : 0}" /></span>
                            </div>
                        </div>
                    </article>
                </div>
            </section>
        </div>
    </main>
</div>

<script>
    const listaPedidosDia = JSON.parse('${pedidosDiaJson}');
    const listaDiasSemana = JSON.parse('${diasSemanaJson}');
    const listaMeses = JSON.parse('${mesesJson}');

    const pedidosDiaHoras = listaPedidosDia.map(p => p.hora);
    const pedidosDiaTotales = listaPedidosDia.map(p => p.total);

    new Chart(document.getElementById('graficoDia').getContext('2d'), {
        type: 'bar',
        data: {
            labels: pedidosDiaHoras,
            datasets: [{
                label: 'Total por pedido S/.',
                data: pedidosDiaTotales,
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                },
                x: {
                    grid: {
                        offset: true
                    }
                }
            }
        }
    });

    const diasSemana = listaDiasSemana.map(d => d.fecha);
    const totalesSemana = listaDiasSemana.map(d => d.total);

    new Chart(document.getElementById('graficoSemana').getContext('2d'), {
        type: 'line',
        data: {
            labels: diasSemana,
            datasets: [{
                label: 'Total por día S/.',
                data: totalesSemana,
                fill: false,
                borderColor: 'rgba(255, 159, 64, 1)',
                tension: 0.2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    const meses = listaMeses.map(m => m.mes);
    const totalesMes = listaMeses.map(m => m.total);

    new Chart(document.getElementById('graficoMes').getContext('2d'), {
        type: 'line',
        data: {
            labels: meses,
            datasets: [{
                label: 'Total mensual S/.',
                data: totalesMes,
                fill: true,
                backgroundColor: 'rgba(75, 192, 192, 0.3)',
                borderColor: 'rgba(75, 192, 192, 1)',
                tension: 0.2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
</script>

</body>
</html>