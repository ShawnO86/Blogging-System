package com.webdev.bloggingsystem.entities;

public record AuthResponseDto(String accessToken, String tokenType) {
    public  AuthResponseDto(String theAccessToken) {
        this(
                theAccessToken,
                "Bearer "
        );
    }
}
