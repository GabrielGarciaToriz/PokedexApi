package com.digis.PokedexApi.security;

import com.digis.PokedexApi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    // Rutas que NO deben pasar por validación JWT
    private static final List<String> RUTAS_PUBLICAS = List.of(
            "/api/auth/**",
            "/api/usuario/agregar",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        AntPathMatcher matcher = new AntPathMatcher();
        return RUTAS_PUBLICAS.stream()
                .anyMatch(patron -> matcher.match(patron, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.esValido(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String correo = jwtService.extraerCorreo(token);
        String rol    = jwtService.extraerRol(token);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                correo,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}