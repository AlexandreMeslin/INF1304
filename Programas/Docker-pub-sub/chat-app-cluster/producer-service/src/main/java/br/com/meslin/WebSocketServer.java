/**
 * WebSocketServer.java
 * This class is responsible for handling WebSocket connections in the chat application.
 * It manages the connection lifecycle and broadcasts messages to all connected clients.
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

import java.util.Date;

/**
 * WebSocketServer class is responsible for handling WebSocket connections in the chat application.
 * It manages the connection lifecycle and broadcasts messages to all connected clients.
 */
@ServerEndpoint("/ws")
public class WebSocketServer {
    private static ChatProducer chatProducer;   // ChatProducer instance for sending messages to Kafka
    private Session session;                    // WebSocket session associated with the current connection
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();    // Thread-safe set to manage active WebSocket connections
    private static Server server;               // Tyrus server instance for managing WebSocket connections

    /** 
     * Starts the WebSocket server and initializes it with the provided ChatProducer instance.
     * @param producer The ChatProducer instance to be used for sending messages to Kafka.
     */
    public static void startServer(ChatProducer producer) {
        chatProducer = producer;
        server = new Server("localhost", 8080, "/chat", null, WebSocketServer.class);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops the WebSocket server.
     */
    public static void stopServer() {
        server.stop();
    }

    /**
     * Handles the event when a new WebSocket connection is established.
     * Adds the new connection to the set of active connections.
     * 
     * @param session The WebSocket session associated with the new connection.
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
    }

    /**
     * Handles incoming messages from WebSocket clients.
     * Sends the received message to the Kafka topic using the ChatProducer and broadcasts it to all connected WebSocket clients.
     * 
     * @param message The message received from the WebSocket client.
     */
    @OnMessage
    public void onMessage(String message) {
        chatProducer.sendMessage(message);
        broadcast(message);
    }

    /**
     * Handles the event when a WebSocket connection is closed.
     * Removes the connection from the set of active connections.
     * 
     * @param session The WebSocket session associated with the closed connection.
     */
    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
    }

    /**
     * Broadcasts a message to all connected WebSocket clients.
     * 
     * @param message The message to be broadcast.
     */
    public static void broadcast(String message) {
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
