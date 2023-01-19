package com.jun.springbootjwt.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
    private List<ErrorField> errors;
    private LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Builder
    public ErrorResponse(
            final int statusCode, final String message, final List<ErrorField> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(final int statusCode, final String message) {
        return new ErrorResponse(statusCode, message);
    }
}