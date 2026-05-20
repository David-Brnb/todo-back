package com.itesm.infrastructure.messaging.kafka;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class KafkaMessageDeserializer extends JsonbDeserializer<KafkaMessage> {
    public KafkaMessageDeserializer() {
        super(KafkaMessage.class);
    }
}