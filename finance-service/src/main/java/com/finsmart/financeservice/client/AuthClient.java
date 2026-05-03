package com.finsmart.financeservice.client;

import com.finsmart.financeservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface AuthClient {
    @GetMapping("/api/auth/users/exists/{id}") // http://localhost:8081/api/auth/users/exists/2
    Boolean checkUserExists(@PathVariable("id") Long id);
}