package com.itesm.infrastructure.persistence.repository;

import com.itesm.application.dto.FirebaseSignInRequest;
import com.itesm.application.dto.FirebaseSignInResponse;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.infrastructure.mapper.UserMapper;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository, PanacheRepositoryBase<UserEntity, UUID> {

    private static final Logger LOG = Logger.getLogger(UserRepositoryImpl.class);
    private static final String FIREBASE_API_KEY = "AIzaSyC0SRe7dw01GW7IgitlCFjyr807uSQD6MY";
    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<User> findByFirebaseUuid(String firebaseUuid) {
        return find("firebaseUuid", firebaseUuid).firstResultOptional().map(this::map);
    }

    @Override
    @Transactional
    public User create(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        persist(userEntity);
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public String login(String email, String password) {
        try {
            URL url = new URL(FIREBASE_AUTH_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            FirebaseSignInRequest request = new FirebaseSignInRequest(email, password, true);
            String jsonBody = objectMapper.writeValueAsString(request);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Firebase login failed with code: " + responseCode);
            }

            FirebaseSignInResponse response = objectMapper.readValue(
                    connection.getInputStream(), FirebaseSignInResponse.class);
            return response.getIdToken();
        } catch (Exception e) {
            LOG.error("Failed to login with Firebase", e);
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    private User map(UserEntity entity) {
        return UserMapper.toDomain(entity);
    }
}
