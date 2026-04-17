package com.finsmart.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // Token'i kara listeye ekle (Süresi kadar Redis'te kalsın)
    public void setBlacklist(String token, long expirationInSeconds) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationInSeconds, TimeUnit.SECONDS);
    }

    public boolean isBlackListed(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
