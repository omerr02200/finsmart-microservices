package com.finsmart.analysisservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsmart.analysisservice.dto.event.AnalysisReportEvent;
import com.finsmart.analysisservice.dto.event.TransactionCreatedEvent;
import com.finsmart.analysisservice.entities.Outbox;
import com.finsmart.analysisservice.entities.OutboxStatus;
import com.finsmart.analysisservice.entities.UserAnalysis;
import com.finsmart.analysisservice.repositories.OutboxRepository;
import com.finsmart.analysisservice.repositories.UserAnalysisRepository;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {

    private final UserAnalysisRepository repository;

    private final OutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;
    private final Tracer tracer;

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

        log.info("Analiz(UserAnalysis tablosu) güncellendi: Kullanıcı: {}", event.userId());

        String analysisDetail = String.format("Sistem Tetiklemesi: Kullanıcının bu işlem sonrası toplam geliri %s, " +
                "toplam gideri %s olmuştur", analysis.getTotalExpense(), analysis.getTotalIncome());

        AnalysisReportEvent aiEvent = new AnalysisReportEvent(
                event.userId().toString(),
                event.category(),
                event.amount(),
                analysisDetail
        );

        String currentTraceId = (tracer.currentTraceContext().context() != null)
                ? tracer.currentTraceContext().context().traceId()
                : "no-trace-id";

        try {
            String jsonPayload = objectMapper.writeValueAsString(aiEvent);

            Outbox outboxRecord = Outbox.builder()
                    .aggregateType("Analysis")
                    .aggregateId(event.userId().toString())
                    .eventType("ANALYSIS_COMPLETED")
                    .payload(jsonPayload)
                    .status(OutboxStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .traceId(currentTraceId)
                    .build();

            outboxRepository.save(outboxRecord);

            log.info("AI Rapor event'i outbox tablosuna PENDING olarak yazıldı. TraceId: {}", currentTraceId);

        } catch (Exception e) {
            log.error("Outbox kayıt hatası: {}", e.getMessage());
            throw new RuntimeException("Outbox yazılamadı, tüm süreç geri alınıyor (Rollback)!", e);
        }
    }

}
