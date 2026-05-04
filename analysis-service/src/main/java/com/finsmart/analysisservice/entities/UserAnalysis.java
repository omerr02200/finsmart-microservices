package com.finsmart.analysisservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_analysis")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserAnalysis {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "total_expense")
    private BigDecimal totalExpense;
    @Column(name = "total_income")
    private BigDecimal totalIncome;
    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    public void addExpense(BigDecimal amount){
        this.totalExpense = this.totalExpense.add(amount);
    }

    public void addIncome(BigDecimal amount){
        this.totalIncome = this.totalIncome.add(amount);
    }
}
