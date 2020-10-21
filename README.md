# OTEL_Kafka

## Prereq

* Download https://github.com/open-telemetry/opentelemetry-java-instrumentation
* Setup Kafka
  * The scripts assume Kafka is running at timemachine:9094. Please change it to your broker host and port
  * The script assumes it has 'sotest' topic with 3 partitions (0-2)
* Setup Jaeger


## How to run

### java_kp (publisher)

```
cd java_kp/src
./gradlew bootJar
java -javaagent:$HOME/Downloads/opentelemetry-javaagent-all.jar -Dotel.exporter=jaeger -Dotel.exporter.jaeger.endpoint=timemachine:14250 -Dotel.exporter.jaeger.service.name=java_kp -jar build/libs/java_kp-1.0-SNAPSHOT.jar
```

### java_kc (consumer)

```
cd java_kc/src
./gradlew bootJar
java -javaagent:$HOME/Downloads/opentelemetry-javaagent-all.jar -Dotel.exporter=jaeger -Dotel.exporter.jaeger.endpoint=timemachine:14250 -Dotel.exporter.jaeger.service.name=java_kc -jar build/libs/java_kc-1.0-SNAPSHOT.jar
```

### py_kc (consumer)

```
pip install kafka-python
python py_kc.py
```

## Notes

### Quick Kafka config with Docker

```
git clone https://github.com/wurstmeister/kafka-docker.git

# edit docker-compose-single-broker.yml as below
# please change ${YOUR DOCKER HOST IP} with your docker host's IP (e.g 192.168.1.2)
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    build: .
    ports:
      - "9092:9092"
      - "9094:9094"
    environment:
      KAFKA_ADVERTISED_LISTENERS: INSIDE://:9092,OUTSIDE://${YOUR DOCKER HOST IP}:9094
      KAFKA_LISTENERS: INSIDE://:9092,OUTSIDE://:9094
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE

      KAFKA_CREATE_TOPICS: "sotest:3:1"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock


```
