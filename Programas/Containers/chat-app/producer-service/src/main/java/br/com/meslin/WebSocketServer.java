/**
 * WebSocketServer.java
 * This class implements a WebSocket server that handles incoming messages and broadcasts them to all connected clients.
 * It uses the ChatProducer class to send messages to a Kafka topic.
 * The server listens for WebSocket connections on the "/ws" endpoint.
 * The server address is "localhost" and the port is 8080, with the context path "/chat".
 * Full server address: ws://localhost:8080/chat/ws
 * 
 * @author  Meslin
 * @version 1.0
 * @since   2024-06-10
 */
package br.com.meslin;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@ServerEndpoint("/ws")  // Acrescenta /ws ao endereço do servidor WebSocket
public class WebSocketServer {
    private static ChatProducer chatProducer;
    private Session session;
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();
    private static Server server;
    private static final Logger logger = LoggerFactory.getLogger(ChatProducer.class);

    /**
     * Starts the WebSocket server and initializes it with the provided ChatProducer instance.
     * 
     * @param producer  The ChatProducer instance to be used for sending messages to Kafka.
     */
    public static void startServer(ChatProducer producer) {
        logger.info("[WebSocketServer.startServer] Starting WebSocket Server.");
        chatProducer = producer;
        server = new Server("localhost", 8080, "/chat", null, WebSocketServer.class);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops the WebSocket server and releases any associated resources.
     * This method should be called when the application is shutting down.
     * It ensures that the WebSocket server is properly stopped and any active connections are closed.
     */
    public static void stopServer() {
        logger.info("[WebSocketServer.stopServer] Stopping WebSocket Server.");
        server.stop();
    }

    /**
     * Handles the event when a new WebSocket connection is established.
     * This method is called when a client connects to the WebSocket server.
     * It adds the new connection to the set of active connections and logs the event.
     * 
     * @param session   The WebSocket session associated with the new connection.
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("[WebSocketServer.onOpen] New connection established.");
        this.session = session;
        connections.add(this);
    }

    /**
     * Handles incoming messages from WebSocket clients.
     * This method is called when a client sends a message to the WebSocket server.
     * It logs the received message, sends it to the Kafka topic using the ChatProducer,
     * and broadcasts the message to all connected WebSocket clients.
     * 
     * @param message   The message received from the client.
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("[WebSocketServer.onMessage] Received message: " + message);
        // Send the message to Kafka
        chatProducer.sendMessage(message);
        // Broadcast the message to all connected WebSocket clients
        broadcast(message);
    }

    /**
     * Handles the event when a WebSocket connection is closed.
     * This method is called when a client disconnects from the WebSocket server.
     * It removes the connection from the set of active connections and logs the event.
     * 
     * @param session   The WebSocket session associated with the closed connection.
     */
    @OnClose
    public void onClose(Session session) {
        logger.info("[WebSocketServer.onClose] Connection closed.");
        connections.remove(this);
    }

    /**
     * Broadcasts a message to all connected WebSocket clients.
     * This method iterates through all active WebSocket connections and sends the provided message
     * to each client. If an error occurs while sending the message to a client, 
     * that client is removed from the set of active connections.
     * 
     * @param message   The message to broadcast.
     */
    public static void broadcast(String message) {
        logger.info("[WebSocketServer.broadcast] Broadcasting message: " + message);
        for (WebSocketServer client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(new Date() + " --> " + message);
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException ex) {
                    // Ignore
                }
            }
        }
    }
}