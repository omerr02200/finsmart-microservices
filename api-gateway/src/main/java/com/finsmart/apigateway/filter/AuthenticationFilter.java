package com.finsmart.apigateway.filter;

import com.finsmart.apigateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final JwtUtils jwtUtils;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    public AuthenticationFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config cfg) {
        return (exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Eksik Authorization Header");
            }

            String autHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String userId = null;
            if(autHeader != null && autHeader.startsWith("Bearer ")) {
                String token = autHeader.substring(7);
                try {
                    Claims claims = jwtUtils.getClaims(token);

                    userId = claims.get("userId").toString();

                } catch (Exception e) {
                    throw new RuntimeException("Geçersiz Token");
                }
            }

            if(userId != null) {
                return chain.filter(exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-User-Id", userId)
                                .header("X-Gateway-Secret", gatewaySecret)
                                .build())
                        .build());
            }
            return chain.filter(exchange);
        };
    }
}
