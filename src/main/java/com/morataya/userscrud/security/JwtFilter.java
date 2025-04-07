package com.morataya.userscrud.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Puedes agregar lógica de inicialización aquí si es necesario
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Configuración de CORS
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // Permitir solicitudes OPTIONS sin validación
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Validación del token JWT
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        // .setSigningKey(jwtSecret.getBytes())
                        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                httpRequest.setAttribute("claims", claims);
            } catch (Exception e) {
              System.out.println("Error al validar el token: " + e.getMessage());
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } else if (!httpRequest.getRequestURI().startsWith("/api/auth")) {
            // Si no es una solicitud a /api/auth y no tiene token, rechazar
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return;
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Puedes agregar lógica de limpieza aquí si es necesario
    }
}