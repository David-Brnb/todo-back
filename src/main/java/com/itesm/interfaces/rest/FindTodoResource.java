package com.itesm.interfaces.rest;

import com.itesm.application.usecase.FindByIdUseCase;
import com.itesm.domain.models.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/find_todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FindTodoResource {
    private final FindByIdUseCase findByIdUseCase;

    @Inject
    public FindTodoResource(FindByIdUseCase findByIdUseCase) { this.findByIdUseCase = findByIdUseCase; }

    @GET
    @Path("/{id}")
    public Response findbyID(@PathParam("id") String id) {
        Todo todo = findByIdUseCase.findBId(id);

        if(todo == null){
            return Response.status(Response.Status.NOT_FOUND).build();

        } else {

            return Response.ok(todo).build();
        }
    }
}
