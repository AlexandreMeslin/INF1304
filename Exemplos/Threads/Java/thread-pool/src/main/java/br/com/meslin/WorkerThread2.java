/**
 * Exemplo de uso de Thread Pool em Java
 * 
 * @author meslin
 */
package br.com.meslin;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.math.BigInteger;
import br.com.meslin.mylib.MyMath;

/**
 * WorkerThread2 implementa Runnable para ser usado em um Thread Pool
 * 
 * @author meslin
 */
public class WorkerThread2 implements Runnable {
    private static int index =0;
    private static final Lock lock = new ReentrantLock();
    private int threadNumber;   // número da thread (para depuração)
    private List<Integer> lBase;
    private List<Integer> lExp;
    private List<Integer> lResp;

    /**
     * Construtor
     * 
     * @param lBase lista de bases
     * @param lExp  lista de expoentes
     * @param lResp lista de respostas
     */
    public WorkerThread2(int threadNumber, List<Integer> lBase, List<Integer> lExp, List<Integer> lResp) {
        this.threadNumber = threadNumber;
        this.lBase = lBase;
        this.lExp = lExp;
        this.lResp = lResp;
    }

    /**
     * Método run() executa a tarefa
     */
    @Override
    public void run() {
        int index;
        while(WorkerThread2.index < this.lBase.size()) {
            // obtém o próximo índice a ser calculado
            synchronized(WorkerThread2.lock) {
                // verifica se ainda há índices a serem calculados
                if(WorkerThread2.index >= this.lBase.size()) break;
                index = WorkerThread2.index;    // obtém o índice atual
                WorkerThread2.index++;          // avança o índice para a próxima thread
            }
    
            // realiza a computação
            int base = this.lBase.get(index);
            int exp = this.lExp.get(index);
            int res = MyMath.power(base, exp);
            //System.out.printf("Calculando %d^%d = %d na posição %d\n", base, exp, res, index);
    
            // devolve a resposta
            if(this.lResp.get(index) == 0) {
                // se a resposta ainda não foi calculada, coloca a resposta no vetor
                this.lResp.set(index, res);
            } else {
                // se alguém já calculou a resposta
                if(this.lBase.get(index) > 0) {
                    // se é o 1o a detectar que a resposta já foi calculada, informa colocando um valor negativo
                    this.lResp.set(index, -1);
                } else {
                    // decrementa o contador se alguma outra thread já percebeu que a resposta já foi calculada
                    this.lResp.set(index, this.lResp.get(index) - 1);
                }
            }
        }
        System.err.println("Thread " + this.threadNumber + " terminou.");
    }
}
