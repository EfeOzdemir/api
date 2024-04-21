package com.plantapp.api.core.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    @Value("${gcp.storage.bucketName}")
    private String bucketName;
    private final Storage storage;

    @Async
    public CompletableFuture<Boolean> upload(MultipartFile file, String key) throws IOException {
        BlobId blobId = BlobId.of(bucketName, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        byte[] content = file.getBytes();
        storage.createFrom(blobInfo, new ByteArrayInputStream(content));
        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    public String generatePublicUrl(String objectName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    public String generateRandomKey() {
        return UUID.randomUUID().toString();
    }

}
