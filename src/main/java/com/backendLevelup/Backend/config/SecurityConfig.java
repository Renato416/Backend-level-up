package com.backendLevelup.Backend.config;

import com.backendLevelup.Backend.security.JwtAuthenticationFilter;
import com.backendLevelup.Backend.security.JwtService;
import com.backendLevelup.Backend.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public SecurityConfig(MyUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, userDetailsService) {
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                String path = request.getRequestURI();
                return path.startsWith("/api/v2/auth/login")
                        || path.startsWith("/api/v2/auth/registro")
                        || path.startsWith("/v3/api-docs")
                        || path.startsWith("/swagger-ui");
            }
        };

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ----------------------------
                        // RUTAS PÚBLICAS
                        // ----------------------------
                        .requestMatchers(
                                "/api/v2/auth/login",
                                "/api/v2/auth/registro",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ----------------------------
                        // PRODUCTOS PÚBLICOS (SOLO GET)
                        // ----------------------------
                        .requestMatchers(HttpMethod.GET, "/api/v2/productos/**")
                        .permitAll()

                        // ----------------------------
                        // PRODUCTOS (ESCRITURA SOLO ADMIN)
                        // ----------------------------
                        .requestMatchers(HttpMethod.POST, "/api/v2/productos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/v2/productos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/v2/productos/**")
                        .hasRole("ADMIN")

                        // ----------------------------
                        // CLIENTE Y ADMIN
                        // ----------------------------
                        .requestMatchers(
                                "/api/v2/carrito/**",
                                "/api/v2/boletas/**"
                        ).hasAnyRole("CLIENTE", "ADMIN")

                        // ----------------------------
                        // ADMIN
                        // ----------------------------
                        .requestMatchers("/api/v2/auth/**")
                        .hasRole("ADMIN")

                        // ----------------------------
                        // CUALQUIER OTRA RUTA
                        // ----------------------------
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ----------------------------
    // CORS
    // ----------------------------
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176",
                "http://localhost:5177"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ----------------------------
    // PASSWORD ENCODER
    // ----------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ----------------------------
    // AUTH MANAGER
    // ----------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
