package com.itesm.infrastructure.messaging.kafka;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaConsumer {

    private static final Logger LOG = Logger.getLogger(KafkaConsumer.class);

    @Incoming("kafka-in")
    @Blocking
    public void process(KafkaMessage msg) {
        LOG.infof("[KafkaConsumer] received: %s", msg.message());
    }
}