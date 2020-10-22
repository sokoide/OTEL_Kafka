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
pip install -r requirements.txt
python py_kc.py
```

## Output examples

* When you run java_kp with OTEL Java Instrumentation, you can see that it injects *traceparent* and 00-_traceid_-01 as below. I'm looking into the format (what 00- and -01 are)

```
[2020-10-21 07:40:15.471734] msg:ConsumerRecord(topic='sotest', partition=0, offset=72, timestamp=1603266015485, timestamp_type=0, key=None, value=b'[2020-10-21 16:40:15.484] hello - 141', headers=[('traceparent', b'00-fbe0369f7400ffea562e756a6435c071-14c98b0a543eecfb-01')], checksum=None, serialized_key_size=-1, serialized_value_size=37, serialized_header_size=66)
k:traceparent, v_hex:30302d66626530333639663734303066666561353632653735366136343335633037312d313463393862306135343365656366622d3031
------------------------------
[2020-10-21 07:40:25.475599] msg:ConsumerRecord(topic='sotest', partition=1, offset=76, timestamp=1603266025489, timestamp_type=0, key=None, value=b'[2020-10-21 16:40:25.489] hello - 142', headers=[('traceparent', b'00-209f928b8480126a805ef9b2ff8dd805-0d55ae883af334a6-01')], checksum=None, serialized_key_size=-1, serialized_value_size=37, serialized_header_size=66)
k:traceparent, v_hex:30302d32303966393238623834383031323661383035656639623266663864643830352d306435356165383833616633333461362d3031
------------------------------
[2020-10-21 07:40:35.480977] msg:ConsumerRecord(topic='sotest', partition=2, offset=61, timestamp=1603266035494, timestamp_type=0, key=None, value=b'[2020-10-21 16:40:35.493] hello - 143', headers=[('traceparent', b'00-8533edf566580d3bb30555f350f3ec1a-5846649a3c692183-01')], checksum=None, serialized_key_size=-1, serialized_value_size=37, serialized_header_size=66)
k:traceparent, v_hex:30302d38353333656466353636353830643362623330353535663335306633656331612d353834363634396133633639323138332d3031
...
```

* When you run java_kp with Brave Kafka Interceptor, TBD

```
TBD
```

* py_kc print like this

```
[2020-10-22 05:10:04.671057] msg:ConsumerRecord(topic='sotest', partition=0, offset=966, timestamp=1603343404671, timestamp_type=0, key=None, value=b'[2020-10-22 14:10:04.67] hello - 73', headers=[('traceparent', b'00-9749f59d118d9592c1a5a8311feb56cc-e37adb3b69cac1cf-01')], checksum=None, serialized_key_size=-1, serialized_value_size=35, serialized_header_size=66)
k:traceparent, v_hex:30302d39373439663539643131386439353932633161356138333131666562353663632d653337616462336236396361633163662d3031
tid:201097446668802158671425316767436330700, sid:16391654841992790479
context:{'current-span': <opentelemetry.trace.span.DefaultSpan object at 0x7f1e0d478a00>}
span:_Span(name="py_kc", context=SpanContext(trace_id=0x9749f59d118d9592c1a5a8311feb56cc, span_id=0x48ba11348793ee35, trace_state=0, is_remote=False))
------------------------------
[2020-10-22 05:10:14.674346] msg:ConsumerRecord(topic='sotest', partition=2, offset=977, timestamp=1603343414674, timestamp_type=0, key=None, value=b'[2020-10-22 14:10:14.674] hello - 74', headers=[('traceparent', b'00-fa034a99b0a78e8e7646ee3e698c67c6-18e98d5095fc960d-01')], checksum=None, serialized_key_size=-1, serialized_value_size=36, serialized_header_size=66)
k:traceparent, v_hex:30302d66613033346139396230613738653865373634366565336536393863363763362d313865393864353039356663393630642d3031
tid:332324088911696033159325742888678877126, sid:1795121303737112077
context:{'current-span': <opentelemetry.trace.span.DefaultSpan object at 0x7f1e0c932700>}
span:_Span(name="py_kc", context=SpanContext(trace_id=0xfa034a99b0a78e8e7646ee3e698c67c6, span_id=0x85550aae2a6de04c, trace_state=0, is_remote=False))
------------------------------
[2020-10-22 05:10:24.677780] msg:ConsumerRecord(topic='sotest', partition=0, offset=967, timestamp=1603343424677, timestamp_type=0, key=None, value=b'[2020-10-22 14:10:24.677] hello - 75', headers=[('traceparent', b'00-dbbe48a30f0f99be435183811a97810c-36a9602394367c38-01')], checksum=None, serialized_key_size=-1, serialized_value_size=36, serialized_header_size=66)
k:traceparent, v_hex:30302d64626265343861333066306639396265343335313833383131613937383130632d333661393630323339343336376333382d3031
tid:292088940732361281227130543846903480588, sid:3938785055038929976
context:{'current-span': <opentelemetry.trace.span.DefaultSpan object at 0x7f1e0c929910>}
span:_Span(name="py_kc", context=SpanContext(trace_id=0xdbbe48a30f0f99be435183811a97810c, span_id=0x684e6bd92a7eb44e, trace_state=0, is_remote=False))
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
