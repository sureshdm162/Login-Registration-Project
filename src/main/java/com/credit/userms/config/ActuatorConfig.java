package com.credit.userms.config;

import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {
    @Bean
    public InMemoryHttpExchangeRepository inMemoryHttpExchangeRepository(){
        return new InMemoryHttpExchangeRepository();
    }
}