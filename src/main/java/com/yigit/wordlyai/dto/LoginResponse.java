package com.yigit.wordlyai.dto;

public record LoginResponse(
        String message,
        UserResponse user
) {
}
