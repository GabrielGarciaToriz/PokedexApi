package com.digis.PokedexApi.config;

import com.digis.PokedexApi.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                // ── Rutas públicas ──────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuario/agregar").permitAll()
                .requestMatchers("/api/catalogo/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/usuario/{id}").permitAll()
                .requestMatchers(
                        "/api/auth/password/forgot",
                        "/api/auth/password/validate",
                        "/api/auth/password/reset"
                ).permitAll()
                // Swagger (si lo usas)
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                // ── MAESTRO_POKEMON: solo sus propios datos ─────────────
                // Ver su propio perfil
                .requestMatchers(HttpMethod.GET, "/api/usuario/username/**").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                //                .requestMatchers(HttpMethod.GET, "/api/usuario/{id}").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                // Sus favoritos
                .requestMatchers(HttpMethod.GET, "/api/favoritos/**").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                .requestMatchers(HttpMethod.POST, "/api/favoritos/**").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                .requestMatchers(HttpMethod.DELETE, "/api/favoritos/**").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                // ── LIDER_POKEMON: gestión completa de usuarios ─────────
                .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("LIDER_POKEMON")
                .requestMatchers(HttpMethod.DELETE, "/api/usuario/**").hasRole("LIDER_POKEMON")
                // ── Pokemones: ambos pueden consultar ───────────────────
                .requestMatchers(HttpMethod.GET, "/api/pokemon/**").hasAnyRole("MAESTRO_POKEMON", "LIDER_POKEMON")
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
