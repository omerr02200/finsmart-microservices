package com.finsmart.authservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsmart.authservice.dto.AuthResponse;
import com.finsmart.authservice.dto.LoginRequest;
import com.finsmart.authservice.dto.RegisterRequest;
import com.finsmart.authservice.entities.Token;
import com.finsmart.authservice.entities.TokenType;
import com.finsmart.authservice.entities.User;
import com.finsmart.authservice.exception.UnauthorizedException;
import com.finsmart.authservice.exception.UserAlreadyExistException;
import com.finsmart.authservice.exception.UserNotFoundException;
import com.finsmart.authservice.repositories.TokenRepository;
import com.finsmart.authservice.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    public String register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            log.error("Kayıt başarısız: Kullancı adı zaten mevcut -> {}", request.getUsername());
            throw new UserAlreadyExistException("Bu kullanıcı adı zaten alınmış");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            log.error("Kayıt başarısız: E-posta zaten mevcut -> {}", request.getEmail());
            throw new UserAlreadyExistException("Bu e-posta adresi ile daha önce kayıt olunmuş");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .isActive(true)
                .build();
        userRepository.save(user);
        return "Kullanıcı başarıyla oluşturuldu";
    }

    public AuthResponse login(LoginRequest request) {

        log.info("Giriş İsteği Geldi : {}", request.username());

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    log.warn("Kullanıcı bulunamadı: {}", request.username());
                    return new UserNotFoundException("Kullanıcı adı veya şifre hatalı");
                });

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Hatalı şifre denemesi: {}", request.username());
            throw new UnauthorizedException("Kullanıcı adı veya şifre hatalı!");
        }

        log.info("Kullanıcı başarıyla giriş yaptı: {}", request.username());

        var accessToken = jwtService.generateToken(user.getUsername());

        revokeAllUserTokens(user);

        saveUserToken(user, accessToken);

        return new AuthResponse(accessToken, accessToken);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return;

        final String refreshToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(refreshToken);
        if(username != null) {
            var user =  userRepository.findByUsername(username).orElseThrow();

            if(jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user.getUsername());

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = new AuthResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
