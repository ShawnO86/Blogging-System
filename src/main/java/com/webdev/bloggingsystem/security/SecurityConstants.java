package com.webdev.bloggingsystem.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class SecurityConstants {
    public static final int JWT_EXPIRY = 70000;
    // todo : change to using environment variable after testing... this will reset token on app restart
    public static final SecretKey JWT_SECRET = Jwts.SIG.HS256.key().build();
}
