package com.example.gatewayservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthWebFilterTest {

    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final JwtAuthWebFilter filter = new JwtAuthWebFilter(jwtUtil);

    @Test
    void filter_deberiaPermitirGetPublicoDeAutosSinToken() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/autos")
        );
        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).isTokenValid(null);
    }

    @Test
    void filter_deberiaRechazarPostDeAutosSinToken() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.method(HttpMethod.POST, "/api/autos")
        );
        WebFilterChain chain = mock(WebFilterChain.class);

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(exchange);
    }

    @Test
    void filter_deberiaPermitirPostDeAutosConTokenValidoParaQueSecurityConfigEvalúeRol() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.method(HttpMethod.POST, "/api/autos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
        );
        WebFilterChain chain = mock(WebFilterChain.class);
        when(jwtUtil.isTokenValid("token-valido")).thenReturn(true);
        when(jwtUtil.getUsername("token-valido")).thenReturn("admin");
        when(jwtUtil.getRole("token-valido")).thenReturn("ADMIN");
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
    }
}
