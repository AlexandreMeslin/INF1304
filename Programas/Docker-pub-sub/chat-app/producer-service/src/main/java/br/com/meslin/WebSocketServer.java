/**
 * Classe responsável por gerenciar o servidor WebSocket.
 * Esta classe lida com as conexões WebSocket, mensagens recebidas e a transmissão de mensagens para todos os clientes conectados.
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

/**
 * Classe responsável por gerenciar o servidor WebSocket.
 * Esta classe lida com as conexões WebSocket, mensagens recebidas 
 * e a transmissão de mensagens para todos os clientes conectados.
 * Ela utiliza a anotação @ServerEndpoint para definir o endpoint do servidor WebSocket 
 * e fornece métodos para lidar com eventos de conexão, mensagens e fechamento de conexão.
 */
@ServerEndpoint("/ws")      /// 
public class WebSocketServer {
    private static ChatProducer chatProducer;   /// Instância do ChatProducer para enviar mensagens para o Kafka.
    private Session session;                    /// Instância da sessão WebSocket associada a esta conexão.
    private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>(); /// Conjunto de conexões WebSocket ativas.
    private static Server server;   /// Instância do servidor WebSocket.
    private static final Logger logger = LoggerFactory.getLogger(ChatProducer.class);   /// Instância do logger para registrar informações e erros.

    /**
     * Inicia o servidor WebSocket e o inicializa com a instância do ChatProducer fornecida.
     * 
     * @param producer Instância do ChatProducer a ser utilizada pelo servidor.
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
     * Para o servidor WebSocket e libera os recursos associados.
     * Este método deve ser chamado quando a aplicação estiver sendo encerrada.
     * Ele garante que o servidor WebSocket seja parado corretamente e que todas as conexões ativas sejam fechadas.
     */
    public static void stopServer() {
        logger.info("[WebSocketServer.stopServer] Stopping WebSocket Server.");
        server.stop();
    }

    /**
     * Método chamado quando uma nova conexão WebSocket é estabelecida.
     * Este método é chamado quando um cliente se conecta ao servidor WebSocket.
     * Ele adiciona a nova conexão ao conjunto de conexões ativas e registra o evento no log.
     * 
     * @param session A sessão WebSocket associada à nova conexão.
     */
    @OnOpen     /// Método chamado quando uma nova conexão WebSocket é estabelecida.
    public void onOpen(Session session) {
        logger.info("[WebSocketServer.onOpen] New connection established.");
        this.session = session;
        connections.add(this);
    }

    /**
     * Método chamado quando uma mensagem é recebida via WebSocket.
     * Este método é chamado quando um cliente envia uma mensagem para o servidor.
     * Ele registra a mensagem no log, envia a mensagem para o produtor Kafka e transmite a mensagem para todos os clientes conectados.
     * 
     * @param message A mensagem recebida via WebSocket.
     */
    @OnMessage      /// Método chamado quando uma mensagem é recebida via WebSocket.
    public void onMessage(String message) {
        logger.info("[WebSocketServer.onMessage] Received message: " + message);
        chatProducer.sendMessage(message);
        broadcast(message);
    }

    /**
     * Método chamado quando uma conexão WebSocket é fechada.
     * Este método é chamado quando um cliente se desconecta do servidor WebSocket.
     * Ele remove a conexão do conjunto de conexões ativas e registra o evento no log.
     * 
     * @param session A sessão WebSocket associada à conexão que foi fechada.
     */
    @OnClose            /// Método chamado quando uma conexão WebSocket é fechada.
    public void onClose(Session session) {
        logger.info("[WebSocketServer.onClose] Connection closed.");
        connections.remove(this);
    }

    /**
     * Método chamado para transmitir uma mensagem para todos os clientes conectados.
     * 
     * @param message A mensagem a ser transmitida.
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