package com.sourabh.auth.dto;


public record AuthResponse(

        String accessToken,
        String refreshToken
) {
}
