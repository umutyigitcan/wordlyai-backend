package com.yigit.wordlyai.dto;

import com.yigit.wordlyai.entity.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String email,
        String biography,
        String profileImagePath,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBiography(),
                user.getProfileImagePath(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
