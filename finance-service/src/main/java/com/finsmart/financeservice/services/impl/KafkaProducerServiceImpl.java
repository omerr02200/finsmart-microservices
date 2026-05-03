package com.finsmart.financeservice.services.impl;

import com.finsmart.financeservice.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String key, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Mesaj başarıyla gönderildi. Topic: {}, Offset: {}",
                        topic, result.getRecordMetadata().offset());
            } else {
                log.error("Mesaj gönderilemedi! Hata: {}", ex.getMessage());
            }
        });
    }
}
