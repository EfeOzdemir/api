package com.plantapp.api.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @Email
        String email,
        @NotNull
        @NotBlank
        String password,
        @NotNull
        @NotBlank
        String username,
        @NotNull
        @NotBlank
        String gender,
        @NotNull
        @NotBlank
        String occupation,
        @NotNull
        @NotBlank
        String city
) {
}
