package com.finsmart.financeservice.services;

import com.finsmart.financeservice.dto.TransactionRequest;
import com.finsmart.financeservice.entities.Transaction;
import com.finsmart.financeservice.entities.TransactionType;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction createTransaction(TransactionRequest request, Long userId);

    List<Transaction> getTransactionsByCategory(Long userId, String category);

    BigDecimal getSummary(Long userId, TransactionType transactionType);

}
