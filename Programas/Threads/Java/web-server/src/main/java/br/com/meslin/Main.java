package br.com.meslin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int port = 8080; // Porta do servidor
        int threadNum = 0;
        String path = args[0];

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado na porta " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept(); // Aceita conexões dos clientes
                    HandleRequest handleRequest = new HandleRequest(clientSocket, path);
                    Thread threadClilent = new Thread(handleRequest);
                    threadClilent.start(); // Trata a requisição do cliente
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}
