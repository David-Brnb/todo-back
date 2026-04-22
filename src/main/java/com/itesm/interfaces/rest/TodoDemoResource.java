package com.itesm.interfaces.rest;

import com.itesm.application.usecase.demo.TodoFetchDemoUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoints didácticos para la demo de eager/lazy/N+1/join fetch.
 * El resource no conoce JPA ni Hibernate — solo delega al use case.
 */
@Path("/todo/demo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoDemoResource {

    private final TodoFetchDemoUseCase useCase;

    @Inject
    public TodoDemoResource(TodoFetchDemoUseCase useCase) {
        this.useCase = useCase;
    }

    @GET
    @Path("/lazy-ok")
    public Response lazyOk() {
        return Response.ok(useCase.runLazyOk()).build();
    }

    @GET
    @Path("/eager-simulation")
    public Response eagerSimulation() {
        return Response.ok(useCase.runEagerSimulation()).build();
    }

    @GET
    @Path("/lazy-n-plus-1")
    public Response lazyNPlusOne() {
        return Response.ok(useCase.runNPlusOne()).build();
    }

    @GET
    @Path("/join-fetch")
    public Response joinFetch() {
        return Response.ok(useCase.runJoinFetch()).build();
    }

    @GET
    @Path("/entity-graph")
    public Response entityGraph() {
        return Response.ok(useCase.runEntityGraph()).build();
    }

    @GET
    @Path("/projection")
    public Response projection() {
        return Response.ok(useCase.runProjection()).build();
    }

    @GET
    @Path("/lazy-init-exception")
    public Response lazyInitException() {
        return Response.ok(useCase.runLazyInitException()).build();
    }
}
