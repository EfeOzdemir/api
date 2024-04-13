package com.plantapp.api.core.service;

import com.plantapp.api.core.model.response.HuggingFaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HuggingFaceService {

    private final RestTemplate hfRestTemplate;

    public HuggingFaceResponse[] query(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file!");

        try {
            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes());
            ResponseEntity<HuggingFaceResponse[]> response
                    = hfRestTemplate.postForEntity("/", request, HuggingFaceResponse[].class);
            return response.getBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}