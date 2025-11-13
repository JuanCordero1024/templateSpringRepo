package com.theworkers.templatemicroservice.service.impl;

import com.theworkers.templatemicroservice.service.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JWTServiceImpl implements JWTService {

    private final Key signingKey;
    private final long expirationMs;

    public JWTServiceImpl(@Value("${jwt.secret-key}") String secretKey,
                          @Value("${jwt.expiration-ms}") long expirationMs) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UUID userId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now+expirationMs))
                .signWith(signingKey)
                .compact();
    }

}
