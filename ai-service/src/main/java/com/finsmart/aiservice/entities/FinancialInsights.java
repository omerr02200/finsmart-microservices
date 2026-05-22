package com.finsmart.aiservice.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "financial_insight")
public class FinancialInsights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String category;
    private BigDecimal totalAmount;

    @Column(columnDefinition = "TEXT")
    private String advice;

    private LocalDateTime createdAt = LocalDateTime.now();
}
