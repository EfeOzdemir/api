package com.plantapp.api.core.service;

import com.plantapp.api.core.model.response.HuggingFaceResponse;
import com.plantapp.api.core.model.response.ModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlantModelService {
    private final ChatService chatService;
    private final HuggingFaceService huggingFaceService;

    public List<ModelResponse> predict(MultipartFile file) {
        HuggingFaceResponse[] predictions = huggingFaceService.query(file);

        List<String> selectedPredictions = Arrays.stream(predictions)
                .limit(3).map(HuggingFaceResponse::label).toList();

        Map<String, String> descriptions = chatService.chat(selectedPredictions);
        List<ModelResponse> result = new ArrayList<>();

        for(Map.Entry<String, String> entry : descriptions.entrySet()) {
            Optional<HuggingFaceResponse> currentPrediction =
                    Arrays.stream(predictions).filter(prediction -> prediction.label().equals(entry.getKey())).findFirst();

            currentPrediction
                    .ifPresent(huggingFaceResponse ->
                            result.add(new ModelResponse(huggingFaceResponse.label(), huggingFaceResponse.score(), entry.getValue())));
        }

        return result.stream().sorted(Comparator.comparingDouble(ModelResponse::score).reversed()).toList();
    }
}
