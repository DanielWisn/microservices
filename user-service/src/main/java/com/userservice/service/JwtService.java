package com.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // In real projects this comes from an env variable / secrets manager, never hardcoded
    private final SecretKey key = Keys.hmacShaKeyFor(
            "this-is-a-placeholder-secret-change-me-to-something-longer-32bytes".getBytes());

    private final long expirationMs = 15 * 60 * 1000; // 15 minutes

    public String generateToken(String username, Long userId) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}