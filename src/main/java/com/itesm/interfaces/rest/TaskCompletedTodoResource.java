package com.itesm.interfaces.rest;

import com.itesm.application.usecase.CompletedTodoUseCase;
import com.itesm.domain.models.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task-done")
@Produces(MediaType.APPLICATION_JSON)
public class TaskCompletedTodoResource {
    private CompletedTodoUseCase completedTodoUseCase;

    @Inject
    public TaskCompletedTodoResource(CompletedTodoUseCase completedTodoUseCase) { this.completedTodoUseCase = completedTodoUseCase; }

    @PUT
    @Path("/{id}")
    public Response setTaskCompleted(@PathParam("id") String id) {
        Todo todo = completedTodoUseCase.setTaskCompleted(id);

        if(todo == null){
            return Response.status(Response.Status.NOT_FOUND).build();

        } else {
            return Response.ok(todo).build();
        }
    }
}
