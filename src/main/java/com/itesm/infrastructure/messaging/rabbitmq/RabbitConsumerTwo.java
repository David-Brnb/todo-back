package com.itesm.infrastructure.messaging.rabbitmq;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RabbitConsumerTwo {

    private static final Logger LOG = Logger.getLogger(RabbitConsumerTwo.class);

    @Incoming("rabbit-two-in")
    public void on(JsonObject json) {
        RabbitMessage event = json.mapTo(RabbitMessage.class);
        LOG.infof("[ConsumerTwo] received: %s", event.message());
    }
}