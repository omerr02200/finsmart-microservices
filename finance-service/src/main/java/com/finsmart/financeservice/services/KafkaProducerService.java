package com.finsmart.financeservice.services;

import com.finsmart.financeservice.dto.event.TransactionCreatedEvent;

public interface KafkaProducerService {
    void sendMessage(String topic, String key, Object message);
}