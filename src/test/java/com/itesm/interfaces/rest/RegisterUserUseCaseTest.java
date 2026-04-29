package com.itesm.interfaces.rest;

import com.itesm.application.usecase.users.RegisterUserUseCase;
import com.itesm.domain.models.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class RegisterUserUseCaseTest {

    @Inject
    RegisterUserUseCase registerUserUseCase;

    @Test
    void registerUser_shouldReturn400WhenEmailIsBlank() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"email\":\"\",\"password\":\"123456\",\"fullName\":\"Juan Perez\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400);

    }

    @Test
    void registerUser_shouldReturn400WhenEmailIsInvalid() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"email\":\"invalid-email\",\"password\":\"123456\",\"fullName\":\"Juan Perez\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    void registerUser_shouldReturn400WhenPasswordIsBlank() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"email\":\"juan@test.com\",\"password\":\"\",\"fullName\":\"Juan Perez\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }


    @Test
    void registerUser_shouldReturn400WhenFullNameIsBlank() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"email\":\"juan@test.com\",\"password\":\"123456\",\"fullName\":\"\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    void register_shouldReturn200WithUser() throws Exception {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User mockUser = new User(userId, null, "Juan Perez", "juan@test.com", true, "fb-123", "USER");

        given()
                .contentType("application/json")
                .body("{\"email\":\"juan@test.com\",\"password\":\"123456\",\"fullName\":\"Juan Perez\"}")
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .body("fullName", equalTo("Juan Perez"))
                .body("email", equalTo("juan@test.com"))
                .body("active", equalTo(true));
    }


}
