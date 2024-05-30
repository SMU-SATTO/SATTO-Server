package com.example.satto.util.dto;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}
