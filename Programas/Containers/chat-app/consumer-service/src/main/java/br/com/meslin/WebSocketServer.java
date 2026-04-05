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

    public static void startServer() {

        server = new Server("localhost", 8080, "/chat", null, WebSocketServer.class);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopServer() {
        server.stop();
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
    }

    @OnMessage
    public void onMessage(String message) {
        
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
    }

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