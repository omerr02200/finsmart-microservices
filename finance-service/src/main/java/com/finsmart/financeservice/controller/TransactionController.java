package com.finsmart.financeservice.controller;

import com.finsmart.financeservice.client.AuthClient;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/finance/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionRepository transactionRepository;

    private final AuthClient authClient;

    @Value("${gateway.secret}")
    private String gateSecret;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Gateway-Secret") String gatewaySecret) {

        if(!gateSecret.equals(gatewaySecret)) {
            throw new RuntimeException("Yetkisiz doğrudan erişim engellendi");
        }

        try {
            Boolean userExists = authClient.checkUserExists(userId);
            if(!Boolean.TRUE.equals(userExists)) {
                throw new RuntimeException("Kullanıcı sistemde kayıtlı değil!");
            }
        } catch (Exception e) {
            log.error("Auth kontrolü başarısız", e.getMessage());
            throw new RuntimeException("Güvenlik kontrolü yapılamadı: " + e.getMessage());
        }
        transaction.setUserId(userId);
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getMyTransactions(@RequestHeader("X-User-Id") long userId) {
        return ResponseEntity.ok(transactionRepository.findByUserId(userId));
    }
}
