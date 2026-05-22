package com.finsmart.aiservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-2.5-flash-lite")
                        .temperature(0.7)
                        .build())
                .build();
    }
}
