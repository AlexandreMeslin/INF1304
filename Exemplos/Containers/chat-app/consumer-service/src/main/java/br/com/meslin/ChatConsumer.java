package br.com.meslin;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.meslin.WebSocketServer;

public class ChatConsumer {
    private static final String TOPIC = "chat-messages";
    private static final String BOOTSTRAP_SERVERS = "kafka:9092";
    private static final String GROUP_ID = "chat-consumer-group";

    private static final Logger logger = LoggerFactory.getLogger(ChatConsumer.class);

    public static void main(String[] args) {
        logger.info("Startint Chat Consumer.");
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", GROUP_ID);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        WebSocketServer.startServer();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Received message: " + record.value());
                    WebSocketServer.broadcast(record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}