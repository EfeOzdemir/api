package com.plantapp.api.configs.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;
import java.io.IOException;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;

@Configuration
@SuppressWarnings("unused")
public class FirebaseConfig {

    @Bean
    @Profile("dev")
    public FirebaseApp firebaseAppDev() throws IOException {
        FirebaseOptions options;
        try (FileInputStream serviceAccount = new FileInputStream("C:\\Users\\Efe Özdemir\\Desktop\\plant-app.json")) {
            options = new FirebaseOptions.Builder()
                    .setCredentials(fromStream(serviceAccount))
                    .build();
        }
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    @Profile("dev")
    public FirebaseAuth firebaseAuthDev() throws IOException {
        return FirebaseAuth.getInstance(firebaseAppDev());
    }

    @Bean
    @Profile("production")
    public FirebaseApp firebaseAppProduction() {
        FirebaseOptions options;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    @Profile("production")
    public FirebaseAuth firebaseAuthProduction() {
        return FirebaseAuth.getInstance(firebaseAppProduction());
    }
}
