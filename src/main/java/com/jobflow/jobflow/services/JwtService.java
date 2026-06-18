package com.jobflow.jobflow.services;

import com.jobflow.jobflow.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtToken;
    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtToken.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }


}
