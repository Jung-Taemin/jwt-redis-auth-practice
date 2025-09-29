package com.example.jwtredisauthpractice.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nickname
) {
}
