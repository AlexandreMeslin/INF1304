/**
 * Exemplo de uso de Thread Pool em Java
 * @author meslin
 */
package br.com.meslin;

import java.util.List;
import br.com.meslin.mylib.MyMath;
import java.math.BigInteger;

/**
 * WorkerThread1 implementa Runnable para ser usado em um Thread Pool
 * @author meslin
 */
public class WorkerThread1 implements Runnable {
    private int index;
    private List<Integer> lBase;
    private List<Integer> lExp;
    private List<Integer> lResp;

    /**
     * Construtor
     * @param index índice do problema a ser resolvido
     * @param lBase lista de bases
     * @param lExp lista de expoentes
     * @param lResp lista de respostas
     */
    public WorkerThread1(int index, List<Integer> lBase, List<Integer> lExp, List<Integer> lResp) {
        this.index = index;
        this.lBase = lBase;
        this.lExp = lExp;
        this.lResp = lResp;
    }

    /**
     * Método run() executa a tarefa
     * 
     * Cada tarefa calcula uma potência e armazena o resultado no vetor de respostas.
     * Se a resposta já foi calculada por outra thread, informa isso colocando um valor negativo
     * no vetor de respostas (isso não deveria acontecer).
     * Note que não há necessidade de sincronização, pois cada thread trabalha em uma posição diferente do vetor.
     * Se duas threads tentarem escrever na mesma posição do vetor, o resultado será um valor negativo.
     */
    @Override
    public void run() {
        int base;
        int exp;
        
        //System.out.println("Thread " + Thread.currentThread().getName() + " calculando índice " + index);
        
        // realiza a computação
        base = lBase.get(index);
        exp = lExp.get(index);
        int res = MyMath.power(base, exp);

        // devolve a resposta
        if(lResp.get(index) == 0) {
            // se a resposta ainda não foi calculada, coloca a resposta no vetor
            lResp.set(index, res);
            //System.out.println("Criando resposta " + base + "^" + exp + " = " + res + " na posição " + index);
        } else {
            // se alguém já calculou a resposta
            if(lResp.get(index) > 0) {
                // se é o 1o a detectar que a resposta já foi calculada, informa colocando um valor negativo
                lResp.set(index, -1);
            } else {
                // decrementa o contador se alguma outra thread já percebeu que a resposta já foi calculada
                lResp.set(index, lResp.get(index) - 1);
            }
        }
        //System.out.println("Thread " + Thread.currentThread().getName() + " terminou índice " + index + " com " + base + "^" + exp + " = " + res);
    }
}
