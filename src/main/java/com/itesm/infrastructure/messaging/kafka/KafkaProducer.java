package com.itesm.infrastructure.messaging.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class KafkaProducer {

    @Channel("kafka-out")
    Emitter<KafkaMessage> emitter;

    public void enqueue(KafkaMessage msg) {
        emitter.send(msg);
    }
}