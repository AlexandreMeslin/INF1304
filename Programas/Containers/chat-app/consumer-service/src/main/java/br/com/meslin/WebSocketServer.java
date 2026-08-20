/**
 * WebSocketServer.java
 * This class implements a WebSocket server that handles real-time communication between clients and the application.
 * It manages WebSocket connections, processes incoming messages, and broadcasts messages to all connected clients.
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

import java.util.Date;

@ServerEndpoint(value = "/ws")
public class WebSocketServer {

    private Session session;
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();
    private static Server server;

    /**
     * Starts the WebSocket server.
     * This method initializes the WebSocket server and starts it on the specified host and port.
     */
    public static void startServer() {
        server = new Server("localhost", 8080, "/chat", null, WebSocketServer.class);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops the WebSocket server.
     * This method stops the WebSocket server and releases any associated resources.
     */
    public static void stopServer() {
        server.stop();
    }

    /**
     * Handles the event when a new WebSocket connection is established.
     * This method is called when a client connects to the WebSocket server.
     * It adds the new connection to the set of active connections.
     *
     * @param session   The WebSocket session associated with the new connection.
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
    }

    /**
     * Handles incoming messages from WebSocket clients.
     * This method is called when a client sends a message to the WebSocket server.
     * It broadcasts the received message to all connected WebSocket clients.
     *
     * @param message   The message received from the client.
     */
    @OnMessage
    public void onMessage(String message) {
        
        broadcast(message);
    }

    /**
     * Handles the event when a WebSocket connection is closed.
     * This method is called when a client disconnects from the WebSocket server.
     * It removes the connection from the set of active connections.
     *
     * @param session   The WebSocket session associated with the closed connection.
     */
    @OnClose
    public void onClose(Session session) {
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
        for (WebSocketServer client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(message);
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