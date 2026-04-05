package br.com.meslin.calculadora.server;

import io.grpc.stub.StreamObserver;

// O serviço
import br.com.meslin.CalcServiceGrpc;
// As mensagens
import br.com.meslin.Conta;
import br.com.meslin.ContaMultipla;
import br.com.meslin.Resultado;
import br.com.meslin.ResultadoMultiplo;

public class CalcServiceImp extends CalcServiceGrpc.CalcServiceImplBase {

    @Override
    public void opera(Conta request, StreamObserver<Resultado> responseObserver) {
        // Obtém os parâmetros
        int operando1 = request.getOperando1();
        int operando2 = request.getOperando2();
        String operacao = request.getOperacao();

        // Realiza a computação
        int valor;
        if(operacao.equals("+")) valor = operando1 + operando2;
        else if(operacao.equals("-")) valor = operando1 - operando2;
        else if(operacao.equals("*")) valor = operando1 * operando2;
        else valor = operando1 / operando2;

        Resultado resultado = Resultado
                .newBuilder()
                .setResultado(valor)
                .build();
        // Envia o resultado
        responseObserver.onNext(resultado);
        // Avisa que o request terminou
        responseObserver.onCompleted();
    }

    @Override
    public void operaVariasVezes(ContaMultipla request, StreamObserver<ResultadoMultiplo> responseObserver) {
        int valor;
        ResultadoMultiplo resultado;

        int operando1 = request.getOperando1();
        int operando2 = request.getOperando2();

        // Constroi e envia o 1o resultado
        valor = operando1 + operando2;
        ResultadoMultiplo resultadoMultiplo = ResultadoMultiplo
                .newBuilder()
                .setOperacao("+")
                .setResultado(valor)
                .build();
        responseObserver.onNext(resultadoMultiplo);

        // Constroi e envia o 2o resultado
        valor = operando1 - operando2;
        resultadoMultiplo = ResultadoMultiplo
                .newBuilder()
                .setOperacao("-")
                .setResultado(valor)
                .build();
        responseObserver.onNext(resultadoMultiplo);

        // Constroi e envia o 3o resultado
        valor = operando1 * operando2;
        resultadoMultiplo = ResultadoMultiplo
                .newBuilder()
                .setOperacao("*")
                .setResultado(valor)
                .build();
        responseObserver.onNext(resultadoMultiplo);

        // Constroi e envia o 4o resultado
        valor = operando1 / operando2;
        resultadoMultiplo = ResultadoMultiplo
                .newBuilder()
                .setOperacao("/")
                .setResultado(valor)
                .build();
        responseObserver.onNext(resultadoMultiplo);

        // Avisa que o request terminou
        responseObserver.onCompleted();
    }    
}
