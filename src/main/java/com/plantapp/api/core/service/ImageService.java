package com.plantapp.api.core.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final String bucketName = "plant-app-community-sharings";

    private final AmazonS3 imageClient;

    public String saveImage(String imageKey, MultipartFile imageFile) {
        if (imageFile.isEmpty()) throw new RuntimeException("Empty image");

        try {
            imageClient.putObject(bucketName, imageKey, imageFile.getInputStream(), null);
            return imageClient.getUrl(bucketName, imageKey).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
