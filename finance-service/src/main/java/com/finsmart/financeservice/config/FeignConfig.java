package com.finsmart.financeservice.config;

import com.finsmart.financeservice.exception.UserNotFoundException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            switch (response.status()) {
                case 404:
                    return new UserNotFoundException("Auth servisi kullanıcıyı bulamadı!");
                case 401:
                    return new RuntimeException("Auth servisi yetki yok hatası!");
                default:
                    return new ErrorDecoder.Default().decode(methodKey, response);
            }
        };
    }
}