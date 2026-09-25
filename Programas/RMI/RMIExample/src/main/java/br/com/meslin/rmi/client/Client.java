/**
 * Client.java
 * 
 * Exemplo de aplicação cliente RMI (Remote Method Invocation).
 * 
 * Este exemplo implementa uma calculadora simples que se conecta a um servidor RMI
 * para realizar operações matemáticas básicas: adição, subtração, multiplicação e divisão.
 * 
 * Para compilar:
 * $ mvn clean install
 * $ sudo docker build -t calculator-rmi-server -f server/Dockerfile .
 * $ sudo docker build -t calculator-rmi-client -f client/Dockerfile .
 * 
 * Para executar o servidor:
 * $ sudo docker run -d --name rmi-server -p 1099:1099 calculator-rmi-server
 * 
 * Para executar o cliente:
 * $ sudo docker run -d --name rmi-client calculator-rmi-client
 *
 * @author Alexandre Meslin
 */
package br.com.meslin.rmi.client;

import java.rmi.Naming;

import br.com.meslin.rmi.shared.Calculator;
import br.com.meslin.rmi.shared.Echo;

public class Client {
    public static void main(String[] args) {
        Calculator calculadora = null;
        Echo echo = null;
        try {
                try {
                    calculadora = (Calculator) Naming.lookup("rmi://rmi-server/Calculator");
                    echo = (Echo) Naming.lookup("rmi://rmi-server/Echo");
                } catch (Exception e) {
                    System.out.println("Seridor RMI ainda não está pronto. Tentando novamente em 1 segundo...");
                    Thread.sleep(1000);
                }

            double a = 12.0;
            double b = 8.0;

            System.out.println(a + " + " + b + " = " + calculadora.adicionar(a, b));
            System.out.println(a + " - " + b + " = " + calculadora.subtrair(a, b));
            System.out.println(a + " x " + b + " = " + calculadora.multiplicar(a, b));
            System.out.println(a + " / " + b + " = " + calculadora.dividir(a, b));

            String message = "Hello, RMI!";
            String echoedMessage = echo.echo(message);
            System.out.println("Echoed message: " + echoedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
