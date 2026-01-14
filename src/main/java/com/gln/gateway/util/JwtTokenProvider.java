package com.gln.gateway.util;

import org.springframework.stereotype.Component;

import com.gln.gateway.config.RsaConfigurationProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

    private final RsaConfigurationProperties rsaConfigurationProperties;

    public JwtTokenProvider(RsaConfigurationProperties rsaConfigurationProperties) {
        this.rsaConfigurationProperties = rsaConfigurationProperties;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(rsaConfigurationProperties.publicKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(rsaConfigurationProperties.publicKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
}
