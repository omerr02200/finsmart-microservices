package com.finsmart.apigateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private final String SECRET_KEY = "fin-smart-cok-gizli-ve-guclu-bir-anahtar-kelime-buraya-cok-daha-uzun-olmalı";

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token).getBody();
    }
}
