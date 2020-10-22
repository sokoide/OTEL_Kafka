package com.sokoide.java_kp;

//import brave.kafka.interceptor.TracingProducerInterceptor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.datatransfer.StringSelection;
import java.sql.Timestamp;
import java.util.Properties;
//import java.util.Collections;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Producer started.");

        // create a producer
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "timemachine:9094");

        // brave interceptor
//        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
//                Collections.singletonList(TracingProducerInterceptor.class));

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties,
                new StringSerializer(), new StringSerializer());

        // send
        try {
            int i = 0;
            while (true) {
                producer.send(new ProducerRecord<>(
                        "sotest",
                        String.format("[%s] hello - %02d",
                                new Timestamp(System.currentTimeMillis()), i))
                );
                i++;
                Thread.sleep(10000);
            }
        } finally {
            producer.close();
        }

//        Thread.sleep(30000);
//        System.out.println("Producer ended.");
    }
}
