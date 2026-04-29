package com.itesm.interfaces.rest;

import com.itesm.application.usecase.todos.DeleteTodoUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/delete_todo")
public class DeleteTodoResource {
    private final DeleteTodoUseCase deleteTodoUseCase;

    @Inject
    public DeleteTodoResource(DeleteTodoUseCase deleteTodoUseCase) { this.deleteTodoUseCase = deleteTodoUseCase; }

    @POST
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") String id) {
        boolean result = deleteTodoUseCase.deleteTodo(id);

        if(result) {
            return Response.ok().build();
        } else  {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}