package com.finsmart.financeservice.repositories;

import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(long userId);

    List<Transaction> findByUserIdAndCategory(Long userId, String category);

    List<Transaction> findByUserIdAndTransactionType(Long userId, TransactionType transactionType);
}