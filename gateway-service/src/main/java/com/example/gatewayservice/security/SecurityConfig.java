package com.example.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/autos/**").permitAll()
                        .pathMatchers("/api/marcas/**").permitAll()
                        .pathMatchers("/api/modelos/**").permitAll()
                        .pathMatchers("/api/categorias/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
