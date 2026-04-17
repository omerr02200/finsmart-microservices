package com.finsmart.authservice.exception;

import java.time.ZonedDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        ZonedDateTime timestamp,
        Map<String,String> validationErrors
) {
    public ErrorResponse(int status, String message) {
        this(status, message, ZonedDateTime.now(), null);
    }
}
