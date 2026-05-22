package com.finsmart.aiservice.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FinancalInsights {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    @Column(columnDefinition = "TEXT")
    private String advice;

    private BigDecimal relevantAmount;

    private LocalDateTime createdAt;
}
