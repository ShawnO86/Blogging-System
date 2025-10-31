package com.webdev.bloggingsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenGenerator {

    public String generateToken(Authentication authentication) {
        //String username = authentication.getPrincipal().toString();
        //or
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + SecurityConstants.JWT_EXPIRY);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SecurityConstants.JWT_SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SecurityConstants.JWT_SECRET).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SecurityConstants.JWT_SECRET).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid token");
        }
    }
}
