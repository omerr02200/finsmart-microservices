package com.finsmart.aiservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class KafkaTracingConfig {

    private final ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory;

    private final KafkaTemplate<?, ?> kafkaTemplate;

    @PostConstruct
    public void init() {
        if(containerFactory != null) {
            containerFactory.getContainerProperties().setObservationEnabled(true);
        }

        if(kafkaTemplate != null) {
            kafkaTemplate.setObservationEnabled(true);
        }
    }
}
