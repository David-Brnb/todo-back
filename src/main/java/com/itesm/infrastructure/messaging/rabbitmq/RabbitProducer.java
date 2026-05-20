package com.itesm.infrastructure.messaging.rabbitmq;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class RabbitProducer {

    @Channel("rabbit-out")
    Emitter<RabbitMessage> emitter;

    public void publish(RabbitMessage event) {
        emitter.send(event);
    }
}