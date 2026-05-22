package com.finsmart.analysisservice.services;

import com.finsmart.analysisservice.dto.event.AnalysisReportEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, AnalysisReportEvent> kafkaTemplate;
    private static final String TOPIC = "analysis-results-topic";

    public void sendToAiService(AnalysisReportEvent event){
        CompletableFuture<SendResult<String, AnalysisReportEvent>> future = kafkaTemplate.send(TOPIC, event.userId(), event);

        future.whenComplete((r, e) -> {
            if (e == null) {
                log.info("Mesaj başarıyla gönderildi. Offset: {}, detay: {}",
                        r.getRecordMetadata().offset(), event.analysisDetail());
            } else {
                log.error("Mesaj gönderilemedi {}", e.getMessage());
            }
        });
    }
}