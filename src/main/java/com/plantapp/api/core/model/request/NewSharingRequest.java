package com.plantapp.api.core.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record NewSharingRequest (
        @NotNull @NotBlank
        String title,
        @NotNull @NotBlank
        String content,
        @NotNull
        MultipartFile image
) {
}
