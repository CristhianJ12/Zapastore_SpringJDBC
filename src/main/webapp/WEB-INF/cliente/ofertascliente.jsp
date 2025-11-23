<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Ofertas y Promociones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
</head>
<body class="light-mode">
<div class="page-container">

    <%@ include file="../fragments/clienteheader.jsp" %>

    <main class="main-content">
        <section class="container section-padding">

            <!-- Hero de promociones principales -->
            <article class="hero-ad-card card">
                <div class="hero-ad-content">
                    <h1 class="hero-ad-title">Ofertas Imperdibles</h1>
                    <p class="hero-ad-lead">Descuentos de temporada en productos seleccionados. ¡Válido hasta agotar stock!</p>

                    <div class="cta-row" style="margin-top: var(--space-6);">
                        <a href="${pageContext.request.contextPath}/catalogo?promo=true">
                            <button class="primary-button"><i class="fa-solid fa-tags"></i> Ver Promociones</button>
                        </a>
                        <a href="${pageContext.request.contextPath}/contacto">
                            <button class="secondary-button" style="margin-left: var(--space-4);">Contáctanos</button>
                        </a>
                    </div>
                </div>

                <div class="hero-ad-media">
                    <div class="promo-badge">-40%</div>
                    <img src="${pageContext.request.contextPath}/img/ad-sample.jpg" alt="Promoción Principal" class="hero-ad-image" style="background-color: #eee;">
                </div>
            </article>

            <!-- Otras promociones -->
            <h2 class="section-title" style="text-align: center; margin-top: var(--space-12);">Otras Promociones</h2>
            <div class="ads-grid-container">

                <article class="small-ad-card card">
                    <div class="card-image-wrapper">
                        <img src="${pageContext.request.contextPath}/img/ad1.jpg" alt="Combo Especial" class="small-ad-image" style="object-fit: cover;">
                    </div>
                    <div class="card-body-content">
                        <h3>Combo Especial</h3>
                        <p>Producto A + Producto B a precio único. ¡Ahorra más!</p>
                        <a href="#" class="color-primary-link">Ver más</a>
                    </div>
                </article>

                <article class="small-ad-card card">
                    <div class="card-body-content">
                        <h3>Lanzamiento Ecológico</h3>
                        <p>Nuevo producto sustentable. Diseño innovador y amigable con el planeta.</p>
                        <a href="#" class="color-primary-link">Descúbrelo</a>
                    </div>
                    <div class="card-image-wrapper">
                        <img src="${pageContext.request.contextPath}/img/ad2.jpg" alt="Lanzamiento" class="small-ad-image" style="object-fit: cover;">
                    </div>
                </article>

                <article class="small-ad-card card">
                    <div class="card-image-wrapper">
                        <img src="${pageContext.request.contextPath}/img/ad3.jpg" alt="Envío Gratis" class="small-ad-image" style="object-fit: cover;">
                    </div>
                    <div class="card-body-content">
                        <h3>Envío Gratis</h3>
                        <p>¡No pagues más! En compras mayores a S/ 99 en todo el Perú.</p>
                        <a href="#" class="color-primary-link">Comprar Ahora</a>
                    </div>
                </article>

            </div>

        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>

</div>
</body>
</html>
