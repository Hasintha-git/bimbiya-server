package com.bimbiya.server.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtEncoder {

    private final String secretKey; // Replace this with your secret key

    public JwtEncoder(String secretKey) {
        this.secretKey = secretKey;
    }

    public String encodeJwtToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
