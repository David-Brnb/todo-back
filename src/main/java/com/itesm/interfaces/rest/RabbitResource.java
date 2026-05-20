package com.itesm.interfaces.rest;

import com.itesm.infrastructure.messaging.rabbitmq.RabbitMessage;
import com.itesm.infrastructure.messaging.rabbitmq.RabbitProducer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rabbit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RabbitResource {

    private final RabbitProducer producer;

    @Inject
    public RabbitResource(RabbitProducer producer) {
        this.producer = producer;
    }

    @POST
    public Response publish(RabbitMessage body) {
        String message = (body == null || body.message() == null || body.message().isBlank())
                ? "rabbit posted"
                : body.message();
        producer.publish(new RabbitMessage(message));
        return Response.accepted().build();
    }
}