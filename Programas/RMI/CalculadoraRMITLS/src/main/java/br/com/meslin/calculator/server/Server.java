package br.com.meslin.calculator.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import br.com.meslin.calculator.shared.Calculator;

/**
 * Classe que representa o servidor da calculadora.
 * Server
 * 
 * Para gerar o certificado autoassinado, utilize o seguinte comando:
 * $ keytool -genkeypair \
 *  -alias calculator-server \
 *  -keyalg RSA \
 *  -keysize 2048 \
 *  -validity 365 \
 *  -keystore ./server/security/server-keystore.p12 \
 *  -storetype PKCS12 \
 *  -storepass changeit \
 *  -keypass changeit \
 *  -dname "CN=calculator-server, OU=INF1304, O=PUC-Rio, L=Rio de Janeiro, ST=RJ, C=BR"
 * 
 * Para exportar o certificado do servidor, utilize o seguinte comando:
 * $ keytool -exportcert \
 *  -alias calculator-server \
 *  -keystore ./server/security/server-keystore.p12 \
 *  -storetype PKCS12 \
 *  -rfc \
 *  -file ./server/security/calculator-server.crt
 */

public class Server {
    static int RMI_PORT = 1099; /// Porta padrão do RMI
    static int PORT = 0; /// Porta para exportar o objeto remoto (0 = porta aleatória)
    public static void main(String[] args) {
        System.out.println("Starting server...");

        try {
            // Configura o socket factory para usar TLS
            SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

            // Cria o registry com suporte a TLS
            Registry registry = LocateRegistry.createRegistry(RMI_PORT, clientSocketFactory, serverSocketFactory);

            // Cria a instância do objeto remoto com suporte a TLS
            Calculator calculadora = new CalculatorImpl(PORT, clientSocketFactory, serverSocketFactory);

            // Registra o objeto remoto no registry
            registry.rebind("Calculator", calculadora);  

            System.out.println("Servidor de calculadora pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
