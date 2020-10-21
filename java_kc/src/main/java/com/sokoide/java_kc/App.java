package com.sokoide.java_kc;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Consumer started.");

        // create a consumer
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "timemachine:9094");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "sokoide-group-1");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties,
                new StringDeserializer(), new StringDeserializer());

        // consume
        consumer.subscribe(Arrays.asList("sotest"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60l));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("t:%s, p:%d, o:%d, value:%s",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.value()));
                    // Sleep per message
                    Thread.sleep(100l);
                }
                // commit the offset
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }
}
