package com.plantapp.api.core.advice;

import com.plantapp.api.core.model.response.ErrorResponse;
import com.plantapp.api.core.exception.CommunitySharingNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseBody
@ControllerAdvice
public class CommunitySharingExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = CommunitySharingNotFoundException.class)
    public ErrorResponse handleNotFoundException(CommunitySharingNotFoundException exception, HttpServletRequest request) {
        return new ErrorResponse(exception.getMessage(), List.of(), HttpStatus.NOT_FOUND.value(), request.getServletPath());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException exception, HttpServletRequest request) {
        return new ErrorResponse(exception.getMessage(), List.of(), HttpStatus.BAD_REQUEST.value(), request.getServletPath());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public ErrorResponse handleAccessException(AccessDeniedException exception, HttpServletRequest request) {
        return new ErrorResponse(exception.getMessage(), List.of(), HttpStatus.FORBIDDEN.value(), request.getServletPath());
    }
}
