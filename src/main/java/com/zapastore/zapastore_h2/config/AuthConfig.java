package com.zapastore.zapastore_h2.config;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/admin/**", "/cliente/**")
                .excludePathPatterns(
                        "/login", "/registrar", "/registro", "/logout",
                        "/css/**", "/img/**", "/js/**"
                );
    }

    // ==============================
    // INTERCEPTOR INTERNO
    // ==============================
    private static class AuthInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioSesion");
            String uri = request.getRequestURI();

            if (usuario == null) {
                response.sendRedirect("/login?error=Debes iniciar sesión");
                return false;
            }

            String rol = usuario.getRol();

            if (uri.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(rol)) {
                response.sendRedirect("/cliente/home?error=No tienes permisos de administrador");
                return false;
            }

            if (uri.startsWith("/cliente")
                    && !("CLIENTE".equalsIgnoreCase(rol) || "ADMIN".equalsIgnoreCase(rol))) {
                response.sendRedirect("/login?error=Rol inválido");
                return false;
            }

            return true;
        }
    }
}
