package br.com.meslin.calculadora.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class CalcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Iniciando o serviço");
        Server server = ServerBuilder.forPort(5003)
                .addService(new CalcServiceImp())
                .build();

        server.start();

        server.awaitTermination();
        System.out.println("Terminando o serviço");
    }    
}
