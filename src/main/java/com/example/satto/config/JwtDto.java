package com.example.satto.config;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}
