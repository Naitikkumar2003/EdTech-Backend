package com.example.EdTech_Backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtService {
    private final String SECRET ="your-secret-api";

    public String generatetoken(UserDetails userDetails){
        return Jwts.builder() //Starts building a JWT token
                .setSubject(userDetails.getUsername()) //the user identifier (email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))//24hr
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()),
                        SignatureAlgorithm.HS256)//Signs the token with your secret key and Uses HMAC-SHA256 algorithm
                .compact(); //Converts everything into a compact string
    }

    public String extractusername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
