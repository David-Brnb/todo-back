package com.itesm.interfaces.rest;

import com.itesm.application.usecase.todos.ListTodoUseCase;
import com.itesm.domain.models.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/list_todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LisTodoResource {
    private ListTodoUseCase ListTodoUseCase;

    @Inject
    public LisTodoResource(ListTodoUseCase ListTodoUseCase) {
        this.ListTodoUseCase = ListTodoUseCase;
    }

    @GET
    public Response listTodo() {
        List<Todo> listTodo = ListTodoUseCase.execute();
        return Response.ok(listTodo).build();
    }
}