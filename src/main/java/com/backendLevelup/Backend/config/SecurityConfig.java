package com.backendLevelup.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // Habilita la configuración de CORS del WebConfig
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas de negocio (ACTUALIZADO A V2)
                        .requestMatchers("/api/v2/productos/**").permitAll()
                        .requestMatchers("/api/v2/categorias/**").permitAll()
                        .requestMatchers("/api/v2/auth/**").permitAll()

                        // Rutas OBLIGATORIAS para que funcione Swagger UI
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}