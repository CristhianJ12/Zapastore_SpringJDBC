package com.zapastore.zapastore_h2.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zapastore.zapastore_h2.model.usuarios.Usuario;

import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
            // Protege todas las rutas de /admin
            .addPathPatterns("/admin/**") 
            // Protege todas las rutas de /cliente
            .addPathPatterns("/cliente/**") 
            // Excluir rutas de autenticación
            .excludePathPatterns("/login", "/registrar", "/register", "/logout", "/css/**", "/img/**"); 
    }

    private static class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            
            String requestURI = request.getRequestURI();
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioSesion");

            // 1. Verificar si el usuario está logeado
            if (usuario == null) {
                // No logeado -> redirigir al login
                response.sendRedirect(request.getContextPath() + "/login?error=Acceso denegado. Inicia sesión.");
                return false;
            }

            // 2. Verificar el rol vs la ruta solicitada
            String rol = usuario.getRol();

            if (requestURI.startsWith(request.getContextPath() + "/admin")) {
                if (!"admin".equalsIgnoreCase(rol)) {
                    // Logeado como cliente intentando acceder a /admin
                    response.sendRedirect(request.getContextPath() + "/cliente/perfil?error=No tienes permisos de Administrador.");
                    return false;
                }
            } else if (requestURI.startsWith(request.getContextPath() + "/cliente")) {
                if (!"cliente".equalsIgnoreCase(rol) && !"admin".equalsIgnoreCase(rol)) {
                    // Logeado con rol no permitido (en un caso más complejo, aquí solo sería si el rol es null o vacío)
                    response.sendRedirect(request.getContextPath() + "/login?error=Rol inválido.");
                    return false;
                }
            }
            
            // Si el usuario está logeado y tiene el rol correcto para la ruta
            return true;
        }
    }
}