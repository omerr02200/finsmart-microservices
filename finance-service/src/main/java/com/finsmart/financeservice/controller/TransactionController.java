package com.finsmart.financeservice.controller;

import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction,
            @RequestHeader("X-User-Id") Long userId) {
        transaction.setUserId(userId);
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getMyTransactions(@RequestHeader("X-User-Id") long userId) {
        return ResponseEntity.ok(transactionRepository.findByUserId(userId));
    }
}
