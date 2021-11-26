package com.craig.pe.demosecuritywebflux.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;


    @Bean
    SecurityWebFilterChain springSecurityWeb(ServerHttpSecurity httpSecurity){
        String[] patterns= new String[] {"/auth/**"};
        return httpSecurity.cors()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }))
                .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and()
                .csrf()
                .disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(patterns)
                .permitAll()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .build();


    }
}
