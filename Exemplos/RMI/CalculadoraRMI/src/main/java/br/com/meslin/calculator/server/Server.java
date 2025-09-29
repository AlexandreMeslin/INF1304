package br.com.meslin.calculator.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import br.com.meslin.calculator.shared.Calculator;

public class Server {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        try {
            LocateRegistry.createRegistry(1099);  // Cria o registro na porta 1099
            Calculator calculadora = new CalculatorImpl();
            Naming.rebind("rmi://localhost/Calculator", calculadora);  // Registra o objeto remoto
            System.out.println("Servidor de calculadora pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
