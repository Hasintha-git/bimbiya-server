package com.bimbiya.server.service.impl;

import com.bimbiya.server.util.RSAKeyProperties;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RSAKeyProperties rsaKeyProperties;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final long accessTokenExpirationMs = 3600000;
    private final long refreshTokenExpirationMs = 604800000;

    public String generateAccessToken(String subject) {
        return generateToken(subject, accessTokenExpirationMs);
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, refreshTokenExpirationMs);
    }

    private String generateToken(String subject, long expirationMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMs);

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, rsaKeyProperties.getPrivateKey())
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true; // Token is valid
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid or expired
        }
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

}
