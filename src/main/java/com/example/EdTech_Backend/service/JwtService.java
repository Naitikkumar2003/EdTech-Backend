package com.example.EdTech_Backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class JwtService {
    private final String SECRET ="mySuperSecureJwtSecretKeyForEdTechPlatform2026";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generatetoken(UserDetails userDetails){
        return Jwts.builder() //Starts building a JWT token
                .setSubject(userDetails.getUsername()) //the user identifier (email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))//24hr
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()),
                        SignatureAlgorithm.HS256)//Signs the token with your secret key and Uses HMAC-SHA256 algorithm
                .compact(); //Converts everything into a compact string
    }



    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
