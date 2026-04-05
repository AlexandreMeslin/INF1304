package br.com.meslin;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatProducer {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    private static final Logger logger = LoggerFactory.getLogger(ChatProducer.class);

    public ChatProducer(String topic) {
        Properties props = new Properties();
        String kafkaBrokers = System.getenv("KAFKA_BROKERS");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 5000);
        props.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 10000);

        producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    public void sendMessage(String message) {
        logger.info("[ChatProducer.sendMessage] " + message);
        message = new Date() + " ==> " + message;
        logger.info("[ChatProducer.sendMessage] " + message);
        producer.send(new ProducerRecord<>(topic, message));
    }

    public void close() {
        producer.close();
    }
    
    public static void main(String[] args) {
        logger.info("Starting Chat Producer.");
        ChatProducer chatProducer = new ChatProducer("chat-messages");
        WebSocketServer.startServer(chatProducer);

        // Keep the application running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down...");
            WebSocketServer.stopServer();
            chatProducer.close();
            logger.info("Shutdown complete.");
        }));

        try {
            // Use a synchronized block to wait indefinitely
            synchronized (ChatProducer.class) {
                ChatProducer.class.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
