package com.itesm.infrastructure.messaging.rabbitmq;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RabbitConsumerOne {

    private static final Logger LOG = Logger.getLogger(RabbitConsumerOne.class);

    @Incoming("rabbit-one-in")
    public void on(JsonObject json) {
        RabbitMessage event = json.mapTo(RabbitMessage.class);
        LOG.infof("[ConsumerOne] received: %s", event.message());
    }
}