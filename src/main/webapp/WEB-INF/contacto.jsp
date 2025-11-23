<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ZapaStore | Contáctanos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;700;900&family=Noto+Sans:wght@400;500;700;900&display=swap" rel="stylesheet">
</head>
<body class="light-mode">
<div class="page-container">

    <%@ include file="fragments/userheader.jsp" %>

    <main class="main-content">
        <section class="container contact-section section-padding">

            <div class="contact-form-container card">
                <h2 class="section-title contact-title">Contáctanos</h2>
                <p class="contact-subtitle">Déjanos tus datos y mensaje. Nos comunicaremos contigo a la brevedad.</p>

                <form action="#" method="post" class="login-form">

                    <div class="form-group">
                        <label for="nombre" class="form-label">Nombre completo</label>
                        <input type="text" name="nombre" id="nombre" class="form-input" placeholder="Tu nombre" required>
                    </div>

                    <div class="form-group">
                        <label for="email" class="form-label">Correo electrónico</label>
                        <input type="email" name="email" id="email" class="form-input" placeholder="ejemplo@correo.com" required>
                    </div>

                    <div class="form-group">
                        <label for="mensaje" class="form-label">Mensaje</label>
                        <textarea name="mensaje" id="mensaje" class="form-input contact-textarea" placeholder="Escribe tu mensaje aquí..." rows="5" required></textarea>
                    </div>

                    <button type="submit" class="primary-button login-button">Enviar Mensaje</button>

                </form>
            </div>

            <div class="contact-info-grid">
                <div class="info-card">
                    <i class="fa-solid fa-phone-volume icon-lg"></i>
                    <h3>Llámanos</h3>
                    <p>+51 987 654 321</p>
                    <p class="text-muted">Lunes a Viernes, 9am - 6pm</p>
                </div>
                <div class="info-card">
                    <i class="fa-solid fa-envelope icon-lg"></i>
                    <h3>Escríbenos</h3>
                    <p>soporte@zapastore.com</p>
                    <p class="text-muted">Respondemos en menos de 24 horas.</p>
                </div>
            </div>

        </section>
    </main>

    <%@ include file="fragments/footer.jsp" %>

</div>
</body>
</html>
