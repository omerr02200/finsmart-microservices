package com.finsmart.financeservice.exception;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.Map;
@Builder
public record ErrorResponse(
        int status,
        String message,
        ZonedDateTime timestamp,
        Map<String,String> valitorErrors
) {
    public ErrorResponse(int status, String message) {
        this(status, message, ZonedDateTime.now(), null);
    }
}
