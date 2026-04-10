package com.itesm.infrastructure.security;

import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class MockFirebaseAuthFilter  implements ContainerRequestFilter {

    @Inject
    AuthenticatedUserContext authenticatedUserContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.equals("/login")) {
            return;
        }

        String authToken = requestContext.getHeaders().getFirst("Authorization");
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        CurrentUser currentUser = new CurrentUser(
                "mock-firebase-uid",
                "test@test.com",
                "USER",
                "Test User",
                UUID.fromString("00000000-0000-0000-0000-000000000001")
        );
        authenticatedUserContext.setCurrentUser(currentUser);
    }
}
