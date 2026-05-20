  ---
# Guía: Kafka + RabbitMQ en Quarkus

## Orden de implementación
1. Docker (levantar brokers)
2. application.properties (configurar canales)
3. Clases Java (event, producer, consumer)
4. Use case (inyectar producer y disparar)

  ---

## Diferencia clave

| | RabbitMQ | Kafka |
  |---|---|---|
| **Pattern** | Fanout (1 evento → N copias) | Queue (1 evento → 1 procesador) |
| **Cuándo usarlo** | Notificaciones a múltiples receptores | Procesamiento async en background |
| **Cómo funciona** | Exchange distribuye copia a cada cola | Consumer lee del topic a su ritmo |
| **Copias** | Cada consumer recibe su propia copia | Un solo consumer por `group.id` procesa cada mensaje |

> Si pones `group.id` diferente en Kafka, ahí sí cada consumer recibe su copia (igual que RabbitMQ fanout).
  
---

## Paso 1: Docker

Archivo: `docker-compose.messaging.yml`

  ```yaml
  version: "3.9"

  services:
    zookeeper:
      image: confluentinc/cp-zookeeper:7.6.1
      environment:
        ZOOKEEPER_CLIENT_PORT: 2181
        ZOOKEEPER_TICK_TIME: 2000
      ports:
        - "2181:2181"
   
    kafka:
      image: confluentinc/cp-kafka:7.6.1
      depends_on:
        - zookeeper
      ports:
        - "9092:9092"
      environment:
        KAFKA_BROKER_ID: 1
        KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
        KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
        KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
        KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
        KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true" 

    rabbitmq:
      image: rabbitmq:3.13-management
      ports: 
        - "5672:5672"
        - "15672:15672"   # UI → http://localhost:15672 (guest/guest)
      environment:
        RABBITMQ_DEFAULT_USER: guest
        RABBITMQ_DEFAULT_PASS: guest

  Comandos:
  docker compose -f docker-compose.messaging.yml up -d   # levantar
  docker compose -f docker-compose.messaging.yml down -v # bajar

  ---
  Paso 2: pom.xml — dependencias

  <!-- Kafka -->
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-messaging-kafka</artifactId>
  </dependency>

  <!-- RabbitMQ -->
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-messaging-rabbitmq</artifactId>
  </dependency>

  ---
  Paso 3: application.properties

  # Desactivar DevServices (usamos docker-compose manual)
  quarkus.kafka.devservices.enabled=false
  quarkus.rabbitmq.devservices.enabled=false

  # ===== Kafka =====
  kafka.bootstrap.servers=${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}

  mp.messaging.outgoing.{canal}-out.connector=smallrye-kafka
  mp.messaging.outgoing.{canal}-out.topic={nombre.topic}
  mp.messaging.outgoing.{canal}-out.value.serializer=io.quarkus.kafka.client.serialization.JsonbSerializer

  mp.messaging.incoming.{canal}-in.connector=smallrye-kafka
  mp.messaging.incoming.{canal}-in.topic={nombre.topic}
  mp.messaging.incoming.{canal}-in.group.id={nombre-app}
  mp.messaging.incoming.{canal}-in.auto.offset.reset=earliest
  mp.messaging.incoming.{canal}-in.value.deserializer={paquete}.{Clase}Deserializer

  # ===== RabbitMQ =====
  rabbitmq-host=${RABBITMQ_HOST:localhost}
  rabbitmq-port=${RABBITMQ_PORT:5672}
  rabbitmq-username=${RABBITMQ_USERNAME:guest}
  rabbitmq-password=${RABBITMQ_PASSWORD:guest}

  # productor
  mp.messaging.outgoing.{canal}-out.connector=smallrye-rabbitmq
  mp.messaging.outgoing.{canal}-out.exchange.name={exchange.name}
  mp.messaging.outgoing.{canal}-out.exchange.type=fanout
  mp.messaging.outgoing.{canal}-out.exchange.declare=true

  # cola 1
  mp.messaging.incoming.{canal-1}-in.connector=smallrye-rabbitmq
  mp.messaging.incoming.{canal-1}-in.queue.name={queue.name.1}
  mp.messaging.incoming.{canal-1}-in.queue.declare=true
  mp.messaging.incoming.{canal-1}-in.exchange.name={exchange.name}
  mp.messaging.incoming.{canal-1}-in.exchange.type=fanout
  mp.messaging.incoming.{canal-1}-in.exchange.declare=true

  # cola 2 (misma estructura, distinto queue.name)
  mp.messaging.incoming.{canal-2}-in.connector=smallrye-rabbitmq
  mp.messaging.incoming.{canal-2}-in.queue.name={queue.name.2}
  ...

  # Tests: in-memory para no necesitar brokers reales
  %test.mp.messaging.outgoing.{canal}-out.connector=smallrye-in-memory
  %test.mp.messaging.incoming.{canal}-in.connector=smallrye-in-memory

  ---
  Paso 4: Clases Java

  RabbitMQ — orden de creación

  1. MiEvento.java             ← record con los campos que viajan por RabbitMQ
  2. MiProducer.java           ← @Channel("canal-out") + Emitter → emitter.send(event)
  3. ConsumerUno.java          ← @Incoming("canal-1-in") → recibe JsonObject, mapea a MiEvento
  4. ConsumerDos.java          ← @Incoming("canal-2-in") → igual, lógica diferente
  5. MiUseCase.java            ← @Inject MiProducer → producer.publish(new MiEvento(...))

  Plantilla Event

  public record MiEvento(Long id, String campo1, String campo2) {}

  Plantilla Producer

  @ApplicationScoped
  public class MiProducer {
      @Channel("canal-out")
      Emitter<MiEvento> emitter;

      public void publish(MiEvento event) {
          emitter.send(event);
      }
  }

  Plantilla Consumer

  @ApplicationScoped
  public class MiConsumer {
      @Incoming("canal-1-in")
      public void on(JsonObject json) {
          MiEvento event = json.mapTo(MiEvento.class);
          // lógica específica de este consumer
      }
  }

  ---
  Kafka — orden de creación

  1. MiMensaje.java            ← record con los datos que viajan por Kafka
  2. MiMensajeDeserializer.java← extends JsonbDeserializer<MiMensaje> — cómo reconstruir el objeto
  3. MiKafkaProducer.java      ← @Channel("canal-out") + Emitter → emitter.send(msg)
  4. MiKafkaConsumer.java      ← @Incoming("canal-in") @Blocking → procesa en background
  5. MiUseCase.java            ← @Inject MiKafkaProducer → producer.enqueue(new MiMensaje(...))

  Plantilla Mensaje

  public record MiMensaje(Long entidadId, String dato1, String dato2) {}

  Plantilla Deserializer

  public class MiMensajeDeserializer extends JsonbDeserializer<MiMensaje> {
      public MiMensajeDeserializer() {
          super(MiMensaje.class);
      }
  }

  Plantilla Producer

  @ApplicationScoped
  public class MiKafkaProducer {
      @Channel("canal-out")
      Emitter<MiMensaje> emitter;

      public void enqueue(MiMensaje msg) {
          emitter.send(msg);
      }
  }

  Plantilla Consumer

  @ApplicationScoped
  public class MiKafkaConsumer {
      @Incoming("canal-in")
      @Blocking
      public void process(MiMensaje msg) {
          // lógica lenta: consultar DB, llamar APIs, etc.
          System.out.println("Procesando id=" + msg.entidadId());
      }
  }

  ---
  Flujos

  RabbitMQ fanout

  POST /endpoint
    → UseCase → guarda en DB → Producer.publish(event)
                                      ↓ exchange fanout
                            ┌─────────┴─────────┐
                      ConsumerUno          ConsumerDos
                    (su propia cola)    (su propia cola)

  Kafka async

  POST /endpoint
    → UseCase → guarda en DB → KafkaProducer.enqueue(msg) → devuelve 202
                                      ↓ topic (background)
                               KafkaConsumer.process(msg)
                                 → hace lo lento
                                 → actualiza DB

  ---
  Estructura de carpetas sugerida

  src/main/java/{paquete}/infrastructure/messaging/
  ├── MiEvento.java                        ← shared entre kafka y rabbit si aplica
  ├── kafka/
  │   ├── MiMensaje.java
  │   ├── MiMensajeDeserializer.java
  │   ├── MiKafkaProducer.java
  │   └── MiKafkaConsumer.java
  └── rabbitmq/
      ├── MiProducer.java
      ├── ConsumerUno.java
      └── ConsumerDos.java