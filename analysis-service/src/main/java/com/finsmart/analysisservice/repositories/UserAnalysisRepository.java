package com.finsmart.analysisservice.repositories;

import com.finsmart.analysisservice.entities.UserAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnalysisRepository extends JpaRepository<UserAnalysis, Long> {
}