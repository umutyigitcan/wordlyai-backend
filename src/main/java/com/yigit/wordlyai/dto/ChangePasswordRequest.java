package com.yigit.wordlyai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password must not be blank")
        String currentPassword,

        @NotBlank(message = "New password must not be blank")
        @Size(min = 8, max = 72, message = "New password must be between 8 and 72 characters")
        String newPassword
) {
}
