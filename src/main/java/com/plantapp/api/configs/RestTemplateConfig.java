package com.plantapp.api.configs;

import com.plantapp.api.configs.properties.HFConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final HFConfigProperties hfConfigProperties;

    @Bean
    public RestTemplate hfRestTemplate() {
        return new RestTemplateBuilder()
                .defaultHeader("Authorization", "Bearer " + hfConfigProperties.token()).rootUri(hfConfigProperties.url()).build();
    }
}
