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
 * Para importar o certificado do servidor no cliente, utilize o seguinte comando:
 * $ keytool -importcert \
 * -alias calculator-server \
 * -file ./server/security/calculator-server.crt \
 * -keystore ./client/security/client-truststore.p12 \
 * -storetype PKCS12 \
 * -storepass changeit 
 *
 * @author Alexandre Meslin
 */
package br.com.meslin.calculator.client;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import br.com.meslin.calculator.shared.Calculator;

public class Client {
    static int PORT = 1099; /// Porta padrão do RMI
    public static void main(String[] args) {
        Calculator calculadora = null;
        try {
            // Configura o socket factory para usar TLS
            SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();

            Registry registry = null;

            while(registry == null) {
                try {
                    registry = LocateRegistry.getRegistry("rmi-server", PORT, clientSocketFactory);
                    calculadora = (Calculator) registry.lookup("Calculator");
                } catch (Exception e) {
                    System.out.println("Falha ao conectar ao servidor RMI: " + e.getClass().getName() + ": " + e.getMessage());
                    System.out.println("Seridor RMI ainda não está pronto. Tentando novamente em 1 segundo...");
                    Thread.sleep(1000);
                }
            }

            double a = 12.0;
            double b = 8.0;

            System.out.println(a + " + " + b + " = " + calculadora.adicionar(a, b));
            System.out.println(a + " - " + b + " = " + calculadora.subtrair(a, b));
            System.out.println(a + " x " + b + " = " + calculadora.multiplicar(a, b));
            System.out.println(a + " / " + b + " = " + calculadora.dividir(a, b));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
