<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="page" value="metricas" />
<c:set var="meses" value="${['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Setiembre','Octubre','Noviembre','Diciembre']}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Métricas de Ventas</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body class="admin-body">
<div class="admin-layout">
    <jsp:include page="/WEB-INF/fragments/sidebar.jsp"/>
    <main class="main-panel">
        <jsp:include page="/WEB-INF/fragments/header.jsp"/>

        <div class="content-wrapper">
            <div class="page-header">
                <h2 class="page-title">
                    <span class="material-symbols-outlined icon-title">analytics</span>
                    Métricas de Ventas
                </h2>
            </div>

            <c:if test="${not empty mensaje}">
                <div class="alert success">${mensaje}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>

            <section class="metrics-area">
                <div class="metrics-grid">

                    <div class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Día</h2>
                            <form action="/admin/metricas" method="get" class="panel-controls">
                                <label for="dia">Día:</label>
                                <input type="date" id="dia" name="dia" value="${diaSeleccionado}" />
                                <input type="hidden" name="semana" value="${fechaSemana}" />
                                <input type="hidden" name="mesInicio" value="${mesInicio}" />
                                <input type="hidden" name="mesFin" value="${mesFin}" />
                                <input type="hidden" name="anio" value="${anio}" />
                                <button type="submit" class="icon-button" title="Filtrar">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoDia"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total día</span>
                                <span class="total-value">S/ <c:out value="${totalDia != null ? totalDia : 0}" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosDia != null ? pedidosDia : 0}" /></span>
                            </div>
                        </div>
                    </div>

                    <div class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Semana</h2>
                            <form action="/admin/metricas" method="get" class="panel-controls">
                                <label for="semana">Semana:</label>
                                <input type="date" id="semana" name="semana" value="${fechaSemana}" />
                                <input type="hidden" name="dia" value="${diaSeleccionado}" />
                                <input type="hidden" name="mesInicio" value="${mesInicio}" />
                                <input type="hidden" name="mesFin" value="${mesFin}" />
                                <input type="hidden" name="anio" value="${anio}" />
                                <button type="submit" class="icon-button" title="Filtrar">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoSemana"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total semana</span>
                                <span class="total-value">S/ <c:out value="${totalSemana != null ? totalSemana : 0}" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosSemana != null ? pedidosSemana : 0}" /></span>
                            </div>
                        </div>
                    </div>

                    <div class="metric-panel">
                        <div class="panel-header">
                            <h2>Ventas por Mes</h2>
                            <form action="/admin/metricas" method="get" class="panel-controls">
                                <label>Desde:</label>
                                <select name="mesInicio">
                                    <c:forEach var="m" items="${meses}" varStatus="status">
                                        <option value="${status.index + 1}" <c:if test="${mesInicio == status.index + 1}">selected</c:if>>${m}</option>
                                    </c:forEach>
                                </select>

                                <label>Hasta:</label>
                                <select name="mesFin">
                                    <c:forEach var="m" items="${meses}" varStatus="status">
                                        <option value="${status.index + 1}" <c:if test="${mesFin == status.index + 1}">selected</c:if>>${m}</option>
                                    </c:forEach>
                                </select>

                                <input type="number" name="anio" value="${anio}" placeholder="Año" style="min-width: 80px;" />
                                <input type="hidden" name="dia" value="${diaSeleccionado}" />
                                <input type="hidden" name="semana" value="${fechaSemana}" />
                                <button type="submit" class="icon-button" title="Filtrar">
                                    <span class="material-symbols-outlined">search</span>
                                </button>
                            </form>
                        </div>
                        <div class="chart-wrap">
                            <canvas id="graficoMes"></canvas>
                        </div>
                        <div class="panel-foot">
                            <div class="metric-total">
                                <span>Total mes</span>
                                <span class="total-value">S/ <c:out value="${totalMes != null ? totalMes : 0}" /></span>
                            </div>
                            <div class="metric-total">
                                <span>Pedidos</span>
                                <span class="total-value"><c:out value="${pedidosMes != null ? pedidosMes : 0}" /></span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main>
</div>

<script type="text/javascript">
    const listaPedidosDia = JSON.parse('${pedidosDiaJson}');
    const listaDiasSemana = JSON.parse('${diasSemanaJson}');
    const listaMeses = JSON.parse('${mesesJson}');

    // Día
    const pedidosDiaHoras = listaPedidosDia.map(p => p.hora);
    const pedidosDiaTotales = listaPedidosDia.map(p => p.total);

    new Chart(document.getElementById('graficoDia').getContext('2d'), {
        type: 'bar',
        data: { labels: pedidosDiaHoras, datasets: [{ label: 'Total por pedido S/.', data: pedidosDiaTotales, backgroundColor: 'rgba(54, 162, 235, 0.7)', borderColor: 'rgba(54, 162, 235, 1)', borderWidth: 1 }] },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true },
                // Añadido para mejorar la alineación de las etiquetas en barras
                x: {
                    grid: {
                        offset: true
                    }
                }
            }
        }
    });

    // Semana
    const diasSemana = listaDiasSemana.map(d => d.fecha);
    const totalesSemana = listaDiasSemana.map(d => d.total);

    new Chart(document.getElementById('graficoSemana').getContext('2d'), {
        type: 'line',
        data: { labels: diasSemana, datasets: [{ label: 'Total por día S/.', data: totalesSemana, fill: false, borderColor: 'rgba(255, 159, 64, 1)', tension: 0.2 }] },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
    });

    // Mes
    const meses = listaMeses.map(m => m.mes);
    const totalesMes = listaMeses.map(m => m.total);

    new Chart(document.getElementById('graficoMes').getContext('2d'), {
        type: 'line',
        data: { labels: meses, datasets: [{ label: 'Total mensual S/.', data: totalesMes, fill: true, backgroundColor: 'rgba(75, 192, 192, 0.3)', borderColor: 'rgba(75, 192, 192, 1)', tension: 0.2 }] },
        options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true } } }
    });
</script>

</body>
</html>