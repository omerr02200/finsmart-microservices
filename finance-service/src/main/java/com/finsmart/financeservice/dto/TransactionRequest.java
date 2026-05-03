package com.finsmart.financeservice.dto;

import com.finsmart.financeservice.entities.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @Positive(message = "Tutar pozitif olmalıdır")
        @NotNull(message = "Tutar boş olamaz")
        BigDecimal amount,

        @NotBlank(message = "Açıklama boş olamaz")
        String description,

        @NotBlank(message = "Kategori boş olamaz")
        String category,

        @NotNull(message = "İşlem tipi seçilmelidir")
        TransactionType transactionType
        ){ }
