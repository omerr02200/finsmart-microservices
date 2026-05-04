package com.finsmart.financeservice.services;

import com.finsmart.financeservice.dto.event.TransactionCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "transaction-events", groupId = "finance-group")
    public void consume(TransactionCreatedEvent event) {
        log.info("### Kafka'dan mesaj geldi! ###");
        log.info("İşlem ID: {}", event.transactionId());
        log.info("İşlem tipi: {}", event.eventType());
        log.info("Miktar: {} {}", event.amount(), event.category());
        log.info("Olay zamanı: {}", event.createdAt());
    }
}
