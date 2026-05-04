package com.finsmart.financeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsmart.financeservice.dto.event.TransactionCreatedEvent;
import com.finsmart.financeservice.entities.Outbox;
import com.finsmart.financeservice.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayService {

    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publisPendindMessages() {
        List<Outbox> pendingMessages = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();

        if (pendingMessages.isEmpty()) {
            return;
        }

        log.info("{} adet bekleyen mesaj Outbox'tan okunuyor...", pendingMessages.size());
        for(Outbox outbox : pendingMessages) {

            try {

                TransactionCreatedEvent event = objectMapper.readValue(outbox.getPayload(), TransactionCreatedEvent.class);

                kafkaProducerService.sendMessage("transaction-events", outbox.getAggregateId(), event);

                outbox.setProcessed(true);

                outboxRepository.save(outbox);

                log.info("Mesaj başarıyla iletildi: ID {}", outbox.getId());

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
