package com.finsmart.analysisservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finsmart.analysisservice.entities.TransactionType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TransactionCreatedEvent(
        String eventId,
        String eventType,
        Long transactionId,
        Long userId,
        BigDecimal amount,
        TransactionType type,
        String category,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        ZonedDateTime createdAt
){
}
