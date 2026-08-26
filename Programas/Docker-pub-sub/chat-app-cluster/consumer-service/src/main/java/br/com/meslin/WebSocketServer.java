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
 * This class is responsible for handling WebSocket connections in the chat application.
 * It manages the connection lifecycle and broadcasts messages to all connected clients.
 * 
 * @author Alexandre Meslin
 * @version 1.0
 */
@ServerEndpoint(value = "/ws")  /// Acrescenta /ws ao endereço do servidor WebSocket
public class WebSocketServer {

    private Session session;    /// Sessão WebSocket associada à conexão atual
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();    /// Conjunto thread-safe para gerenciar conexões WebSocket ativas
    private static Server server;   /// Instância do servidor Tyrus para gerenciar conexões WebSocket

    /**
     * Inicia o servidor WebSocket.
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
     * Para o servidor WebSocket.
     */
    public static void stopServer() {
        server.stop();
    }


    /**
     * Manipula o evento quando uma nova conexão WebSocket é estabelecida.
     * Adiciona a nova conexão ao conjunto de conexões ativas.
     * 
     * @param session
     */
    @OnOpen     /// Anotação para indicar que este método será chamado quando uma nova conexão WebSocket for aberta
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
    }

    /**
     * Manipula mensagens recebidas de clientes WebSocket.
     * Transmite a mensagem recebida para todos os clientes WebSocket conectados.
     * 
     * @param message
     */
    @OnMessage      /// Anotação para indicar que este método será chamado quando uma mensagem for recebida de um cliente WebSocket
    public void onMessage(String message) {
        broadcast(message);
    }

    /**
     * Manipula o evento quando uma conexão WebSocket é fechada.
     * Remove a conexão do conjunto de conexões ativas.
     * 
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
    }

    /**
     * Transmite uma mensagem para todos os clientes WebSocket conectados.
     * 
     * @param message
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
