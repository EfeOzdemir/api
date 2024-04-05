package com.plantapp.api.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gcp.vertexai")
public record VertexAIConfig(String projectId, String location, String modelName) {
}
