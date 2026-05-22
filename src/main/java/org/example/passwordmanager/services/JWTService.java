package org.example.passwordmanager.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSigningKey;

    public String extractUserName(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignKey())
                .compact();
    }

    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
    }
}
