package com.finsmart.aiservice.repositories;

import com.finsmart.aiservice.entities.FinancialInsights;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialInsightsRepository extends JpaRepository<FinancialInsights, Long> {
    List<FinancialInsights> findByUserIdOrderByCreatedAtDesc(String userId);
}