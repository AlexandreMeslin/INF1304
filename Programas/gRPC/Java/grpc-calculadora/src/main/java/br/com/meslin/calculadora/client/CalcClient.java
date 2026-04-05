/**
 * Exemplo de aplicação gRPC em Java - Cliente.<br>
 * Calculadora.
 * 
 * Para compilar:<br>
 * $ mvn clean package
 * 
 * Para executar o servidor:<br>
 * $ java -jar target/grpc-server.jar
 * 
 * Para executar o cliente:<br>
 * $ java -jar target/grpc-client.jar
 * 
 * @note Observação: este exemplo não usa criptografia (TLS/SSL).
 * @note A comunicação é feita em texto puro (plaintext).
 * @note Em um ambiente de produção, é recomendável usar TLS/SSL.
 * @author Meslin
 * @seealso br.com.meslin.calculadora.server.CalcServer
 * @seealso https://grpc.io/docs/languages/java/basics/
 * @seealso https://grpc.io/docs/guides/auth/
 */
package br.com.meslin.calculadora.client;

import br.com.meslin.CalcServiceGrpc;
import br.com.meslin.Conta;
import br.com.meslin.ContaMultipla;
import br.com.meslin.Resultado;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Cliente gRPC - Calculadora
 * @author Meslin
 */
public class CalcClient {
    final static int PORTA = 5003;  // Porta do servidor gRPC
    /**
     * Método principal.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Cria o canal de comunicação com o servidor gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", PORTA)
                .usePlaintext()         // (para usar TLS, remover a chamada .usePlaintext())
                .build();

        // Cria o stub (cliente)
        CalcServiceGrpc.CalcServiceBlockingStub stub = CalcServiceGrpc.newBlockingStub(channel);

        int operando1 = 87;
        int operando2 = 52;

        // Chama o método remoto "opera" do servidor
        // Exemplo de chamada com resultado único
        System.out.println("Resultado único");
        Conta conta = Conta.newBuilder()
                .setOperacao("+")
                .setOperando1(operando1)
                .setOperando2(operando2)
                .build();
        // Chamada remota
        Resultado resultado = stub.opera(conta);
        System.out.println(operando1 + " + " + operando2 + " = " + resultado.getResultado());

        // Chama o método remoto "operaVariasVezes" do servidor
        // Exemplo de chamada com resultado múltiplo (streaming)
        System.out.println("Resultado múltiplo");
        ContaMultipla contaMultipla = ContaMultipla.newBuilder()
                .setOperando1(operando1)
                .setOperando2(operando2)
                .build();
        // Chamada remota
        stub.operaVariasVezes(contaMultipla).forEachRemaining(resultadoMultiplo -> {
            System.out.println(operando1 + " " + resultadoMultiplo.getOperacao() + " " + operando2 + " = " + resultadoMultiplo.getResultado());
        });
    }
}
