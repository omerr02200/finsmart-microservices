package com.finsmart.authservice.controller;

import com.finsmart.authservice.dto.AuthResponse;
import com.finsmart.authservice.dto.LoginRequest;
import com.finsmart.authservice.dto.RegisterRequest;
import com.finsmart.authservice.repositories.UserRepository;
import com.finsmart.authservice.services.AuthService;
import com.finsmart.authservice.services.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);
            redisService.setBlacklist(token, 86400);
        }
        return ResponseEntity.ok("Başarıyla çıkış yapıldı");
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request,response);
    }

    @GetMapping("/users/exists/{id}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable("id") Long id){
        return ResponseEntity.ok(userRepository.existsById(id));
    }
}
