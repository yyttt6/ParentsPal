package com.example.main.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FirebaseInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            initializeFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C:\\test-d2255-firebase-adminsdk-k2j0v-a345e73151.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

}