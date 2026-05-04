package com.finsmart.analysisservice.services;

import com.finsmart.analysisservice.dto.event.TransactionCreatedEvent;
import com.finsmart.analysisservice.entities.UserAnalysis;
import com.finsmart.analysisservice.repositories.UserAnalysisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {

    private final UserAnalysisRepository repository;

    @Transactional
    public void updateAnalysis(TransactionCreatedEvent event){
        UserAnalysis analysis = repository.findById(event.userId())
                .orElse(UserAnalysis.builder()
                        .id(event.userId())
                        .totalExpense(BigDecimal.ZERO)
                        .totalIncome(BigDecimal.ZERO)
                        .build());

        if ("EXPENSE".equalsIgnoreCase(String.valueOf(event.type()))){
            //analysis.setTotalExpense(analysis.getTotalExpense().add(event.amount()));
            analysis.addExpense(event.amount());
            log.info("Harcama eklendi. Yeni toplam: {}", analysis.getTotalExpense());
        } else if("INCOME".equalsIgnoreCase(String.valueOf(event.type()))) {
            //analysis.setTotalIncome(analysis.getTotalIncome().add(event.amount()));
            analysis.addIncome(event.amount());
            log.info("Gelir eklendi. Yeni toplam: {}", analysis.getTotalExpense());
        } else {
            log.warn("Bilinmeyen işlem tipi: {}", event.type());
        }

        analysis.setLastTransactionDate(event.createdAt().toLocalDateTime());

        repository.save(analysis);

        log.info("Analiz güncellendi: Kullanıcı: {}, Yeni Toplam Harcama: {}",
                event.userId(), analysis.getTotalIncome());
    }

}
