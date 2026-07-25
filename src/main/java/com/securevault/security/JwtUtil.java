package com.securevault.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "mysecurevaultsecretkeymysecurevaultsecretkey";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
}

public boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
}

public boolean validateToken(String token, String email) {

    return extractUsername(token).equals(email)
            && !isTokenExpired(token);

}

}