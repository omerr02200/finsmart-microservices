package com.finsmart.analysisservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsmart.analysisservice.dto.event.AnalysisReportEvent;
import com.finsmart.analysisservice.entities.Outbox;
import com.finsmart.analysisservice.entities.OutboxStatus;
import com.finsmart.analysisservice.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxMessage() {

        List<Outbox> pendingMessages = outboxRepository.findByStatus(OutboxStatus.PENDING);

        if (pendingMessages.isEmpty()) {
            return;
        }

        log.info("OutboxScheduler çalıştı. İşlenecek mesaj sayısı: {}", pendingMessages.size());

        for(Outbox outbox : pendingMessages) {
            try {

                AnalysisReportEvent event = objectMapper.readValue(outbox.getPayload(), AnalysisReportEvent.class);
                kafkaProducerService.sendToAiService(event);
                outbox.setStatus(OutboxStatus.SENT);
                outboxRepository.save(outbox);

                log.info("Outbox mesajı Kafka'ya uçuruldu ve SENT olarak güncellendi. ID: {}",  outbox.getId());

            } catch (Exception e) {
                log.error("Outbox mesajı kafkaya gönderilirken hata oluştu, " +
                        "ID: {}. Hata: {}", outbox.getId(), e.getMessage());

                outbox.setStatus(OutboxStatus.FAILED);
                outboxRepository.save(outbox);
            }
        }
    }
}
