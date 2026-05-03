package com.finsmart.financeservice.services.impl;

import com.finsmart.financeservice.client.AuthClient;
import com.finsmart.financeservice.dto.TransactionRequest;
import com.finsmart.financeservice.dto.event.TransactionCreatedEvent;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;
import com.finsmart.financeservice.exception.UserNotFoundException;
import com.finsmart.financeservice.repositories.TransactionRepository;
import com.finsmart.financeservice.services.KafkaProducerService;
import com.finsmart.financeservice.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
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

    private final KafkaProducerService  kafkaProducerService;

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

        kafkaProducerService.sendMessage("transaction-events", transaction.getUserId().toString(), event);

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
