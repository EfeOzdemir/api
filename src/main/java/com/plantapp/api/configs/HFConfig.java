package com.plantapp.api.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hf.api")
public record HFConfig(String url, String token) {
}
