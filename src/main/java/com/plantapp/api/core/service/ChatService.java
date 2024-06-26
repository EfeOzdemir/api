package com.plantapp.api.core.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.plantapp.api.configs.properties.VertexAIConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final VertexAIConfigProperties vertexAIConfigProperties;
    private final String content =
            "Give me the treatment and symptoms of the '%s' disease. Make it a bullet list. Do not include any external links.";

    public Map<String, String> chat(List<String> prompts) {
        try (VertexAI vertexAI = new VertexAI(vertexAIConfigProperties.projectId(), vertexAIConfigProperties.location())) {
            GenerativeModel model = new GenerativeModel(vertexAIConfigProperties.modelName(), vertexAI);
            Map<String, String> results = new HashMap<>();

            prompts.parallelStream().forEach(prompt -> {
                try {
                    GenerateContentResponse response = model.generateContent(String.format(content, prompt));
                    results.put(prompt, ResponseHandler.getText(response));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
            return results;
        }
    }
}
