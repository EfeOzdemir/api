package com.plantapp.api.configs.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@SuppressWarnings("unused")
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try (FileInputStream serviceAccount = new FileInputStream("src/plant-app-firebase-adminsdk.json")) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                return FirebaseApp.initializeApp(options);
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance(firebaseApp());
    }
}
