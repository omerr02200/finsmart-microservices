package com.finsmart.financeservice.controller;

import com.finsmart.financeservice.dto.TransactionRequest;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;
import com.finsmart.financeservice.exception.InvalidCredentialsException;
import com.finsmart.financeservice.exception.UserNotFoundException;
import com.finsmart.financeservice.repositories.TransactionRepository;
import com.finsmart.financeservice.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/finance/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Value("${gateway.secret}")
    private String gateSecret;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Gateway-Secret") String gatewaySecret) {

        if(!gateSecret.equals(gatewaySecret)) {
            throw new InvalidCredentialsException("Yetkisiz doğrudan erişim engellendi");
        }

        return ResponseEntity.ok(transactionService.createTransaction(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getMyTransactions(@RequestHeader("X-User-Id") long userId) {
        return ResponseEntity.ok(transactionRepository.findByUserId(userId));
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Transaction>> getByCategory(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("category") String category) {


        return ResponseEntity.ok(transactionService.getTransactionsByCategory(userId, category));
    }

    @GetMapping("/summary")
    public ResponseEntity<BigDecimal> getSummary(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam TransactionType transactionType
    ) {

        return ResponseEntity.ok(transactionService.getSummary(userId, transactionType));
    }
}