package com.plantapp.api.core.controller;

import com.plantapp.api.core.dto.ModelResponse;
import com.plantapp.api.core.service.PlantModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantModelService plantModelService;

    @PostMapping("/predict")
    public List<ModelResponse> predict(@RequestParam("image") MultipartFile file) {
        return plantModelService.predict(file);
    }
}
