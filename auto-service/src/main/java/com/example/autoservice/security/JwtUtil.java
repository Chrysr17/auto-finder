package com.example.autoservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public String getRolesFromToken(String token) {
        Object role = getAllClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    public boolean isTokenValid(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDate(String token) {
        return getAllClaims(token).getExpiration();
    }

}
