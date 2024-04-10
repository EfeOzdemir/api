package com.plantapp.api.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gcp.vertexai")
public record VertexAIConfigProperties(String projectId, String location, String modelName) {
}
