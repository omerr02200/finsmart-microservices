package com.finsmart.financeservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

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
    public static TransactionCreatedEvent of(Transaction transaction) {
        return new TransactionCreatedEvent(
                UUID.randomUUID().toString(),
                "TRANSACTION_CREATED",
                transaction.getId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getCategory(),
                ZonedDateTime.now()
        );
    }
}
