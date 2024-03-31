package com.plantapp.api.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HuggingFaceService {

    private static final String API_URL = "https://api-inference.huggingface.co/models/Professor/Plant_Classification_model_vit-base-patch16-224-in21k";
    private static final String API_TOKEN = "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";

    RestTemplate restTemplate = new RestTemplate();

//    public String query(MultipartFile file) throws IOException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization" , "Bearer " + API_TOKEN );
//        HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);
//        ResponseEntity<String> response
//                = restTemplate.postForEntity(API_URL, request, String.class);
//        return response.getBody();
//    }

    public String query(MultipartFile file) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
        connection.setRequestProperty("Content-Type", "image/jpeg");
        connection.setDoOutput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(file.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

}