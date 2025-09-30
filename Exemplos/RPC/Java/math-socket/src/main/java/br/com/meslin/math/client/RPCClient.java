/**
 * @author Alexandre Meslin
 * @file RPCClient.java
 * @brief Cliente RPC simples em Java usando sockets
 */
package br.com.meslin.math.client;

import java.io.*;
import java.net.*;
import br.com.meslin.math.commun.MathService;

public class RPCClient {
    final static int PORT = 12345; // Porta do servidor

    public static void main(String[] args) throws Exception {
        System.out.println("Chamando soma(5, 3) remotamente...");
        int r1 = call("soma", 5, 3);
        System.out.println("Resultado: " + r1);

        System.out.println("Chamando multiplica(4, 6) remotamente...");
        int r2 = call("multiplica", 4, 6);
        System.out.println("Resultado: " + r2);
    }

    private static int call(String metodo, int a, int b) throws Exception {
        Socket socket = new Socket("localhost", PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // envia requisição no formato "metodo,a,b"
        out.println(metodo + "," + a + "," + b);

        // lê resposta
        String resposta = in.readLine();

        socket.close();
        return Integer.parseInt(resposta);
    }
}
