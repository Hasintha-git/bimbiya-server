package com.bimbiya.server.service.impl;

import com.bimbiya.server.util.RSAKeyProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RSAKeyProperties rsaKeyProperties;

    private final String secretKey = "your-secret-key"; // Replace this with your secret key

    public String generateJwtToken(Authentication authentication) {
        String subject = authentication.getName();
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour in milliseconds

        String jwt = Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, rsaKeyProperties.getPrivateKey())  // Use RSA-SHA256 with the private key
                .compact();


        boolean b = validateJwtToken(jwt);

        return jwt;
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true; // Token is valid
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid or expired
        }
    }


//    public String generateToken(Authentication authentication) {
//        Instant now = Instant.now();
//
//        String scope= authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(""));
//
//        JWTClaimsSet claimsSet= JWTClaimsSet.Builder()
//                .issuer("self")
//                .issuedAt(now)
//                .subject(authentication.getName())
//                .claim("roles",scope)
//                .build();
//
//        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
//    }

}
