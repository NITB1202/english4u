package com.nitb.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtSecurityContextRepository contextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(contextRepository)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/webjars/**",
                                "/swagger/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/api/auth/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}