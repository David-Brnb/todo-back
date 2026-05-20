package com.itesm.interfaces.rest;

import com.itesm.infrastructure.messaging.kafka.KafkaMessage;
import com.itesm.infrastructure.messaging.kafka.KafkaProducer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/kafka")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KafkaResource {

    private final KafkaProducer producer;

    @Inject
    public KafkaResource(KafkaProducer producer) {
        this.producer = producer;
    }

    @POST
    public Response publish(KafkaMessage body) {
        String message = (body == null || body.message() == null || body.message().isBlank())
                ? "kafka posted"
                : body.message();
        producer.enqueue(new KafkaMessage(message));
        return Response.accepted().build();
    }
}