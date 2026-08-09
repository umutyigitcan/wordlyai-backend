package com.yigit.wordlyai.dto;

public record LoginResponse(
        String message,
        String token,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
}
