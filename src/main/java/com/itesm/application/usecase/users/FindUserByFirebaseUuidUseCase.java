package com.itesm.application.usecase.users;

import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FindUserByFirebaseUuidUseCase {

    private final UserRepository userRepository;

    @Inject
    public FindUserByFirebaseUuidUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(String firebaseUuid) {
        if (firebaseUuid == null || firebaseUuid.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByFirebaseUuid(firebaseUuid);
    }
}

