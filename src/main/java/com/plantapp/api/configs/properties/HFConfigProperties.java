package com.plantapp.api.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hf.api")
public record HFConfigProperties(String url, String token) {
}
