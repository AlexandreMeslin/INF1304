package br.com.meslin.calculadora.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Servidor gRPC - Calculadora
 * @author Meslin
 */
public class CalcServer {
    final static int PORTA = 5003;  // Porta do servidor gRPC

    /**
     * Método principal.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Iniciando o serviço");
        Server server = ServerBuilder.forPort(PORTA)
                .addService(new CalcServiceImp())
                .build();

        server.start();

        server.awaitTermination();
        System.out.println("Terminando o serviço");
    }    
}
