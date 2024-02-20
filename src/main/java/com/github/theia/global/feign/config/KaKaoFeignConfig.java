package com.github.theia.global.feign.config;

import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaKaoFeignConfig {
    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
}
