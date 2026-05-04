package com.finsmart.analysisservice.services;

import com.finsmart.analysisservice.dto.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final AnalysisService analysisService;

    @KafkaListener(topics = "transaction-events", groupId = "analysis-group")
    public void consume(TransactionCreatedEvent event) {
        log.info("### Analysis Service: Yeni harcama yakalandı! ###");
        log.info("Kullanıcı: {}, Miktar: {}, Kategori: {}, Transaction ID: {}",
                event.userId(), event.amount(), event.category(), event.transactionId());

        analysisService.updateAnalysis(event);

    }
}