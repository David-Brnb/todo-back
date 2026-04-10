package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateTodoDTO;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.application.usecase.CreateTodoUseCase;
import com.itesm.domain.models.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {
    private final CreateTodoUseCase createTodoUseCase;
    private final AuthenticatedUserContext authenticatedUserContext;

    @Inject
    public TodoResource(CreateTodoUseCase createTodoUseCase, AuthenticatedUserContext authenticatedUserContext) {
        this.createTodoUseCase = createTodoUseCase;
        this.authenticatedUserContext = authenticatedUserContext;
    }
    @POST
    public Response createTodo(CreateTodoDTO createTodoDTO){
        Todo todo = createTodoUseCase.execute(createTodoDTO);
        return Response.ok(todo).build();
    }

    @GET
    public Response getTodo(){
        CurrentUser currentUser = authenticatedUserContext.getCurrentUser();
        System.out.println(currentUser.getFullName());
        return Response.ok("Reponse").build();
    }

}