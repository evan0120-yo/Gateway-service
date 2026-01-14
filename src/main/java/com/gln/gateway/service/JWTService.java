package com.gln.gateway.service;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gln.gateway.config.RsaConfigurationProperties;
import com.gln.gateway.exception.DataNotFoundException;
import com.gln.gateway.model.Account;
import com.gln.gateway.repository.AccountRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {

    private final RsaConfigurationProperties rsaConfigurationProperties;

    public JWTService(RsaConfigurationProperties rsaConfigurationProperties) {
        this.rsaConfigurationProperties = rsaConfigurationProperties;
    }

    @Autowired
    private AccountRepository accountRepository;

    public String findAccountIdByToken(String token) {
        String username = extractUsername(token);
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new DataNotFoundException("User not found: " + username));
        return account.getAccountId();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(rsaConfigurationProperties.privateKey(), SignatureAlgorithm.RS256)
            .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(rsaConfigurationProperties.publicKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
