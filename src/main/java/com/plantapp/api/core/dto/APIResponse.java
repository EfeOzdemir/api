package com.plantapp.api.core.dto;

import java.time.Instant;

public record APIResponse<T>(int httpStatus, String message, T data, Instant timestamp) {
    public APIResponse(int httpStatus, String message, T data) {
        this(httpStatus, message, data, Instant.now());
    }
}
