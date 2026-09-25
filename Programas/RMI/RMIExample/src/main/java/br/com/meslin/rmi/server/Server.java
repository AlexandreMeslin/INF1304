package br.com.meslin.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import br.com.meslin.rmi.shared.Calculator;
import br.com.meslin.rmi.shared.Echo;

public class Server {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        try {
            LocateRegistry.createRegistry(1099);  // Cria o registry na porta 1099
            Calculator calculadora = new CalculatorImpl();
            Echo echo = new EchoImpl();
            Naming.rebind("rmi://0.0.0.0/Calculator", calculadora);  // Registra o objeto remoto
            Naming.rebind("rmi://0.0.0.0/Echo", echo);  // Registra o objeto remoto
            System.out.println("Servidor de calculadora & echo pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
