package com.example.restservice.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String SECRET_KEY = "YWRkYWI2NmZiY2M2ODZkOTFlYTllNWQ5YjkzYTg2ZTk0ODRmNWMzMTZiMWVhZmVkNzhiZjcxODBiYmFjNzZmOQ=="; 

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Validité de 10 heures
                .signWith(getSignKey(), SignatureAlgorithm.HS256) 
                .compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isValidJwt(String token) {
        try {
            // Vérifie si le JWT est bien formé et valide
            Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
            
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT est expiré : " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT non supporté : " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT mal formé : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur de validation du JWT : " + e.getMessage());
        }
        return false;
    }
    
}