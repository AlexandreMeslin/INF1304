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

    /**
     * Constructor for ChatProducer.
     * 
     * @param topic The Kafka topic to which messages will be sent.
     */
    public ChatProducer(String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    /**
     * Sends a message to the Kafka topic.
     * 
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        logger.info("[ChatProducer.sendMessage] " + message);
        message = new Date() + " ==> " + message;
        logger.info("[ChatProducer.sendMessage] " + message);
        producer.send(new ProducerRecord<>(topic, message));
    }

    /**
     * Closes the Kafka producer to release resources.
     */
    public void close() {
        producer.close();
    }
    
    /**
     * Main method to start the ChatProducer and WebSocket server.
     * 
     * @param args  Command line arguments (not used).
     */
    public static void main(String[] args) {
        logger.info("[ChatProducer.main] Starting Chat Producer.");

        // Initialize ChatProducer with the topic "chat-messages"
        ChatProducer chatProducer = new ChatProducer("chat-messages");

        // Start the WebSocket server and pass the ChatProducer instance to it
        WebSocketServer.startServer(chatProducer);

        // Add a shutdown hook to gracefully close the producer and stop the WebSocket server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("[ChatProducer.main] Shutting down...");
            WebSocketServer.stopServer();
            chatProducer.close();
            logger.info("[ChatProducer.main] Shutdown complete.");
        }));

        // Keep the application running
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