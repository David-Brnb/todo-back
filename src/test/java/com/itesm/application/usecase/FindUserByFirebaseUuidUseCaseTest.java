package com.itesm.application.usecase;

import com.itesm.application.usecase.users.FindUserByFirebaseUuidUseCase;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FindUserByFirebaseUuidUseCaseTest {

    @Test
    void execute_shouldReturnUserWhenFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        FindUserByFirebaseUuidUseCase useCase = new FindUserByFirebaseUuidUseCase(userRepository);

        String firebaseUuid = "fb-123";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirebaseUuid(firebaseUuid);
        user.setEmail("a@b.com");
        user.setFullName("Alice");
        user.setActive(true);
        user.setRole("USER");

        when(userRepository.findByFirebaseUuid(firebaseUuid)).thenReturn(Optional.of(user));

        Optional<User> result = useCase.execute(firebaseUuid);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFullName());
    }

    @Test
    void execute_shouldReturnEmptyWhenNotFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        FindUserByFirebaseUuidUseCase useCase = new FindUserByFirebaseUuidUseCase(userRepository);

        when(userRepository.findByFirebaseUuid("fb-missing")).thenReturn(Optional.empty());

        Optional<User> result = useCase.execute("fb-missing");
        assertTrue(result.isEmpty());
    }
}

