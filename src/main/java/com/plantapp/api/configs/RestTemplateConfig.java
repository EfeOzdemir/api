package com.plantapp.api.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final HFConfig hfConfig;

    @Bean
    public RestTemplate hfRestTemplate() {
        return new RestTemplateBuilder()
                .defaultHeader("Authorization", "Bearer " + hfConfig.token()).rootUri(hfConfig.url()).build();
    }
}
