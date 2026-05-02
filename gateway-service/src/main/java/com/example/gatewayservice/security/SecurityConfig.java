package com.example.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthWebFilter jwtAuthWebFilter;

    public SecurityConfig(JwtAuthWebFilter jwtAuthWebFilter) {
        this.jwtAuthWebFilter = jwtAuthWebFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers("/api/auth", "/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/autos", "/api/autos/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/marcas", "/api/marcas/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/modelos", "/api/modelos/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/categorias", "/api/categorias/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/autos", "/api/autos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/autos", "/api/autos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/autos", "/api/autos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/marcas", "/api/marcas/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/marcas", "/api/marcas/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/marcas", "/api/marcas/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/modelos", "/api/modelos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/modelos", "/api/modelos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/modelos", "/api/modelos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/categorias", "/api/categorias/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/categorias", "/api/categorias/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/categorias", "/api/categorias/**").hasRole("ADMIN")
                        .pathMatchers("/api/comparar", "/api/comparar/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
