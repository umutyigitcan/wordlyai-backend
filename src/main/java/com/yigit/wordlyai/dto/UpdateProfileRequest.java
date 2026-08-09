package com.yigit.wordlyai.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 500, message = "Biography must not exceed 500 characters")
        String biography,

        @Size(max = 500, message = "Profile image path must not exceed 500 characters")
        String profileImagePath
) {
}
