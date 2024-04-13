package com.plantapp.api.core.model.response;

import java.util.List;

public record ErrorResponse (String message, List<String> errors, int httpStatus, String path) { }
