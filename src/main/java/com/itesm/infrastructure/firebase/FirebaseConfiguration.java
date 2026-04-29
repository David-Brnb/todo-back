package com.itesm.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.FileInputStream;
import java.io.InputStream;

@Startup
@ApplicationScoped
@UnlessBuildProfile("test")
public class FirebaseConfiguration {
    @ConfigProperty(name="quarkus.google.cloud.service-account-location")
    String path;

    @PostConstruct
    void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Intentar cargar desde el Classpath (ideal para archivos en src/main/resources)
                InputStream serviceAccount = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

                // Si no lo encuentra en resources, intenta buscarlo como archivo físico (fuera del jar)
                if (serviceAccount == null) {
                    serviceAccount = new FileInputStream(path);
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId("todo-list-58e01")
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado correctamente");
            }
        } catch (Exception e) {
            // Importante: No solo imprimas el error, lanza una excepción para que Quarkus se detenga si falla
            throw new RuntimeException("Fallo al configurar Firebase: " + e.getMessage(), e);
        }
    }
}