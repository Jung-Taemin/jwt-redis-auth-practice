package com.example.jwtredisauthpractice.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
