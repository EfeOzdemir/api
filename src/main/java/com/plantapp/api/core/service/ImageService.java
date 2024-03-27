package com.plantapp.api.core.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.plantapp.api.core.exception.ImageCouldNotSavedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${bucketName}")
    private String bucket;

    private final AmazonS3 imageClient;

    public String saveImage(String imageName, MultipartFile imageFile) {
        if (imageFile.isEmpty()) throw new IllegalArgumentException("Empty file!");

        try {
            String key = createImageKey(imageName);
            imageClient.putObject(bucket, key, imageFile.getInputStream(), null);
            return imageClient.getUrl(bucket, key).toString();
        } catch (AmazonClientException | IOException e) {
            throw new ImageCouldNotSavedException("An error occurred save!");
        }
    }

    private String createImageKey(String title) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.format("%s:%s:%s", userId, title, Instant.now().toString());
    }
}
