/**
 * ChatProducer.java
 * This class is responsible for producing messages to a Kafka topic.
 * It uses the KafkaProducer from the Apache Kafka client library to send messages.
 * The class is designed to be used in a chat application where messages are sent to a Kafka
 * 
 */
package br.com.meslin;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChatProducer class is responsible for sending messages to a specified Kafka topic.
 * It initializes a KafkaProducer with the necessary configurations and provides methods to send messages and close the producer.
 * The class also includes a main method to start the ChatProducer and WebSocket server.
 */
public class ChatProducer {
    private final KafkaProducer<String, String> producer;   // Kafka producer instance for sending messages
    private final String topic;                             // Kafka topic to which messages will be sent

    private static final Logger logger = LoggerFactory.getLogger(ChatProducer.class);   // Logger instance for logging information and errors

    /**
     * Constructor for ChatProducer.
     * Initializes the KafkaProducer with the necessary configurations and sets the topic for message sending.
     * 
     * @param topic The Kafka topic to which messages will be sent.
     */
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

    /**
     * Sends a message to the Kafka topic.
     * The message is prefixed with the current date and time before being sent.
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
     * This method should be called when the producer is no longer needed to ensure proper resource management.
     */
    public void close() {
        producer.close();
    }

    /**
     * Main method to start the ChatProducer and WebSocket server.
     * It initializes the ChatProducer with the specified Kafka topic and starts the WebSocket server.
     * The application will keep running until it is terminated, at which point the WebSocket server and ChatProducer will be properly closed.  
     */
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
