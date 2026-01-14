package com.gln.gateway.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateJwt(Authentication auth) {
        Instant now = Instant.now();
        String roles = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        JwtClaimsSet jwtClaimSet = JwtClaimsSet.builder()
            .issuer("gateway-service")
            .issuedAt(now)
            .subject(auth.getName())
            .claim("scope", roles)
            .expiresAt(now.plus(30, ChronoUnit.MINUTES))
            .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimSet)).getTokenValue();
    }
}
