package com.finsmart.financeservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsmart.financeservice.client.AuthClient;
import com.finsmart.financeservice.dto.TransactionRequest;
import com.finsmart.financeservice.dto.event.TransactionCreatedEvent;
import com.finsmart.financeservice.entities.Outbox;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;
import com.finsmart.financeservice.exception.UserNotFoundException;
import com.finsmart.financeservice.repositories.OutboxRepository;
import com.finsmart.financeservice.repositories.TransactionRepository;
import com.finsmart.financeservice.services.KafkaProducerService;
import com.finsmart.financeservice.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthClient  authClient;

    //private final KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper;

    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public Transaction createTransaction(TransactionRequest request, Long userId) {

        Boolean userExists = authClient.checkUserExists(userId);
        if(!Boolean.TRUE.equals(userExists)) {
            throw new UserNotFoundException("Kullanıcı doğrulaması başarısız!");
        }

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .description(request.description())
                .category(request.category())
                .transactionType(request.transactionType())
                .userId(userId)
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        TransactionCreatedEvent event = TransactionCreatedEvent.of(transaction);

        try {
            String jsonPayload = objectMapper.writeValueAsString(event);

            Outbox outbox = Outbox.builder()
                    .aggregateType("Transaction")
                    .aggregateId(transaction.getId().toString())
                    .type("TRANSACTION_CREATED")
                    .payload(jsonPayload)
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(outbox);

            log.info("Mesaj outbox tablosuna kaydedildi, iletilmeyi bekliyor");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON dönüştürme hatası", e);
        }

        //kafkaProducerService.sendMessage("transaction-events", transaction.getUserId().toString(), event);

        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByCategory(Long userId, String category) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndCategory(userId, category);
        return transactions;
    }

    @Override
    public BigDecimal getSummary(Long userId, TransactionType transactionType) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionType(userId, transactionType);

        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }
}
