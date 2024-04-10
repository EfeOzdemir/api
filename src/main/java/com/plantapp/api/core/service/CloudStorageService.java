package com.plantapp.api.core.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    @Value("gcp.storage.bucketName")
    private String bucketName;
    private final Storage storage;

    public String upload(MultipartFile file, String key) throws IOException {
        BlobId blobId = BlobId.of(bucketName, getImageName(key));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] content = file.getBytes();
        storage.createFrom(blobInfo, new ByteArrayInputStream(content));
        return blobInfo.getMediaLink();
    }

    private String getImageName(String key) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.format("%s:%s:%s", userId, key, Instant.now().toString());
    }

}
