/**
 * @author Alexandre Meslin
 * @file RPCServer.java
 * @brief Servidor RPC simples em Java usando sockets
 */
package br.com.meslin.math.server;

import java.io.*;
import java.net.*;
import br.com.meslin.math.commun.MathService;

/**
 * Servidor RPC implementando MathService
 * Escuta requisições de clientes e processa chamadas remotas.
 */
public class RPCServer implements MathService {
    private static final int PORT = 12345; // Porta do servidor
    
    /**
     * Método principal do servidor RPC
     * Inicia o servidor e aguarda conexões de clientes.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(PORT);                  // Porta do servidor
        System.out.println("Servidor RPC rodando na porta " + PORT + "...");

        while (true) {
            Socket socket = server.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

    /**
     * Manipula a comunicação com o cliente
     * Recebe a requisição, processa e envia a resposta.
     * 
     * @param socket Socket do cliente
     */
    private static void handleClient(Socket socket) {
        MathService service = new RPCServer();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            String request = in.readLine();
            System.out.println("Requisição recebida: " + request);

            String[] parts = request.split(",");
            String metodo = parts[0];
            int a = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);

            int resultado;
            if (metodo.equals("soma")) {
                resultado = service.soma(a, b);
            } else if (metodo.equals("multiplica")) {
                resultado = service.multiplica(a, b);
            } else {
                resultado = 0;
            }

            out.println(resultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementação do método soma
     * 
     * @param a Primeiro inteiro
     * @param b Segundo inteiro
     * @return Soma de a e b
     */
    @Override
    public int soma(int a, int b) { 
        return a + b; 
    }

    /**
     * Implementação do método multiplica
     * 
     * @param a Primeiro inteiro
     * @param b Segundo inteiro
     * @return Produto de a e b
     */
    @Override
    public int multiplica(int a, int b) { 
        return a * b; 
    }
}
