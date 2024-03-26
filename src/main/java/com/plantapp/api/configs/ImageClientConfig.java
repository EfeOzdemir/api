package com.plantapp.api.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageClientConfig {
    @Value("${accessKey}")
    private String accessKey;
    @Value("${secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public AmazonS3 imageClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretAccessKey)
                ))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }
}
