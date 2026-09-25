package br.com.meslin.rmitls.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import br.com.meslin.rmitls.shared.Calculator;
import br.com.meslin.rmitls.shared.Echo;

/**
 * Classe que representa o servidor RMI.
 * Server
 * 
 * Para gerar o certificado autoassinado, utilize o seguinte comando:
 * $ keytool -genkeypair \
 *   -alias rmitls-server \
 *   -keyalg RSA \
 *   -keysize 2048 \
 *   -validity 365 \
 *   -keystore ./server/security/server-keystore.p12 \
 *   -storetype PKCS12 \
 *   -storepass changeit \
 *   -keypass changeit \
 *   -dname "CN=rmi-server, OU=INF1304, O=PUC-Rio, L=Rio de Janeiro, ST=RJ, C=BR" \
 *   -ext "SAN=dns:rmi-server,dns:rmitls-server"
 * 
 * Para exportar o certificado do servidor, utilize o seguinte comando:
 * $ keytool -exportcert \
 *   -alias rmitls-server \
 *   -keystore ./server/security/server-keystore.p12 \
 *   -storetype PKCS12 \
 *   -storepass changeit \
 *   -rfc \
 *   -file ./server/security/rmitls-server.crt
 */

public class Server {
    static int RMI_PORT = 1099; /// Porta padrão do RMI
    static int PORT = 0; /// Porta para exportar o objeto remoto (0 = porta aleatória)
    static String HOSTNAME = "rmi-server"; /// Nome do host do servidor

    public static void main(String[] args) {
        System.out.println("Starting server...");

        // Para resolver o nome do host de acordo com o nome que está no certificado
        System.setProperty("java.rmi.server.hostname", HOSTNAME);

        try {
            // Configura o socket factory para usar TLS
            SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

            // Cria o registry com suporte a TLS
            Registry registry = LocateRegistry.createRegistry(RMI_PORT, clientSocketFactory, serverSocketFactory);

            // Cria a instância do objeto remoto com suporte a TLS
            Calculator calculadora = new CalculatorImpl(PORT, clientSocketFactory, serverSocketFactory);
            Echo echo = new EchoImpl(PORT, clientSocketFactory, serverSocketFactory);

            // Registra o objeto remoto no registry
            registry.rebind("Calculator", calculadora);  
            registry.rebind("Echo", echo);

            System.out.println("Servidor RMI com TLS pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
