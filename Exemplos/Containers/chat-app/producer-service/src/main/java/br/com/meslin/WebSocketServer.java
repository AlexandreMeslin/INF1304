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

@ServerEndpoint("/ws")
public class WebSocketServer {
    private static ChatProducer chatProducer;
    private Session session;
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();
    private static Server server;
    private static final Logger logger = LoggerFactory.getLogger(ChatProducer.class);

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

    public static void stopServer() {
        logger.info("[WebSocketServer.stopServer] Stopping WebSocket Server.");
        server.stop();
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("[WebSocketServer.onOpen] New connection established.");
        this.session = session;
        connections.add(this);
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("[WebSocketServer.onMessage] Received message: " + message);
        chatProducer.sendMessage(message);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("[WebSocketServer.onClose] Connection closed.");
        connections.remove(this);
    }

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