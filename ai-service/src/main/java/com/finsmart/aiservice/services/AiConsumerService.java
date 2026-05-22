package com.finsmart.aiservice.services;

import com.finsmart.aiservice.dto.event.AnalysisReportEvent;
import com.finsmart.aiservice.entities.FinancialInsights;
import com.finsmart.aiservice.repositories.FinancialInsightsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiConsumerService {

    private final ChatClient  chatClient;
    private final FinancialInsightsRepository repository;

    @KafkaListener(topics = "analysis-results-topic", groupId = "ai-group")
    @Transactional
    public void consume(AnalysisReportEvent event) {

        log.info("Kafka'dan analiz verisi alındı. UserID: {}", event.userId());

        String prompt = String.format("Kullanıcı %s kategorisinde %s TL harcama yaptı. Detay : %s. " +
                "Bu verilere dayanarak kısa ve etkili bir finansal tavsiye ver.",
                event.category(), event.totalAmount(), event.analysisDetail());

        try {
            String aiResponse = chatClient.prompt(prompt).call().content();

            FinancialInsights insight = new FinancialInsights();
            insight.setUserId(event.userId());
            insight.setCategory(event.category());
            insight.setTotalAmount(event.totalAmount());
            insight.setAdvice(aiResponse);

            repository.save(insight);
            log.info("Finansal görü (insight) başarıyla kaydedildi");

        } catch (Exception e) {
            log.error("AI servisi hatası (Muhtemelen Kota/429): {}", e.getMessage());
        }
    }
}
