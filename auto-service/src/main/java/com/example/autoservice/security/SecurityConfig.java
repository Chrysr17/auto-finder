package com.example.autoservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       httpSecurity
               .csrf(csrf -> csrf.disable())
               .sessionManagement(sm ->
                       sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers(
                               "/v3/api-docs/**",
                               "/swagger-ui/**",
                               "/swagger-ui.html",
                               "/actuator/health"
                       ).permitAll()

                       .requestMatchers(HttpMethod.GET, "/api/autos/**").permitAll()
                       .requestMatchers(HttpMethod.GET, "/api/marcas/**").permitAll()
                       .requestMatchers(HttpMethod.GET, "/api/modelos/**").permitAll()
                       .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                       .requestMatchers(HttpMethod.POST, "/api/autos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.PUT, "/api/autos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/autos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.POST, "/api/marcas/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.PUT, "/api/marcas/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/marcas/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.POST, "/api/modelos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.PUT, "/api/modelos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/modelos/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")

                       .anyRequest().authenticated()
               )
               .formLogin(form -> form.disable())
               .httpBasic(basic -> basic.disable());

       httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

       return httpSecurity.build();
    }

}
