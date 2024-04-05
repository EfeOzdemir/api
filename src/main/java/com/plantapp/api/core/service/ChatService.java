package com.plantapp.api.core.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.preview.ChatSession;
import com.google.cloud.vertexai.generativeai.preview.GenerativeModel;
import com.google.cloud.vertexai.generativeai.preview.ResponseHandler;
import com.plantapp.api.configs.VertexAIConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final VertexAIConfig vertexAIConfig;

    public Map<String, String> chat(List<String> prompts) {
        try (VertexAI vertexAI = new VertexAI(vertexAIConfig.projectId(), vertexAIConfig.location())) {;
            GenerativeModel model = new GenerativeModel(vertexAIConfig.modelName(), vertexAI);
            Map<String, String> results = new HashMap<>();

            prompts.parallelStream().forEach(prompt -> {
                try {
                    ChatSession chatSession = new ChatSession(model);
                    GenerateContentResponse response = chatSession.sendMessage(prompt);
                    results.put(prompt, ResponseHandler.getText(response));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });

            return results;
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
