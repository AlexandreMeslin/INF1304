package br.com.meslin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import br.com.meslin.mylib.MyMath;

/**
 * Exemplo de uso de Thread Pool em Java
 * 
 * Compilar com:
 * mvn compile
 * 
 * Executar com:
 * java -cp target/classes br.com.meslin.Main
 * 
 * @author meslin
 */
public class Main {
    private static final long DEFAULT_PROBLEM_SIZE = 100;
    private static final int DEFAULT_POOL_SIZE = 5;

    private static long problemSize = DEFAULT_PROBLEM_SIZE;
    private static int poolSize = DEFAULT_POOL_SIZE;

    /** vetor de bases */
    private static List<Integer> lBase;
    /** vetor de expoentes */
    private static List<Integer> lExp;
    /** vetor de respostas */
    private static List<Integer> lResp;

    /**
     * Função principal
     * <ul>
     *  <li>Cria os vetores</li>
     *  <li>Inicializa os vetores</li>
     *  <li>Executa os cálculos usando ExecutorService</li>
     *  <li>Verifica o resultado</li>
     *  <li>Zera o vetor de respostas</li>
     *  <li>Executa os cálculos usando pool de threads manual</li>
     *  <li>Verifica o resultado</li>
     *  <li>Zera o vetor de respostas</li>
     *  <li>Executa os cálculos usando método sequencial (sem threads)</li>
     *  <li>Verifica o resultado</li>
     *  <li>Termina</li>
     * </ul>
     * 
     * @param args
     */
    public static void main(String[] args) {
        Main.problemSize = (args.length > 0) ? Integer.parseInt(args[0]) : (int) DEFAULT_PROBLEM_SIZE;
        Main.poolSize = (args.length > 1) ? Integer.parseInt(args[1]) : DEFAULT_POOL_SIZE;

        System.out.println("Começando...");
        System.out.println("Tamanho do problema: " + Main.problemSize);
        System.out.println("Tamanho do pool: " + Main.poolSize);

        // cria os vetores
        Main.lBase = new ArrayList<Integer>();
        Main.lExp = new ArrayList<Integer>();
        Main.lResp = new ArrayList<Integer>();

        // inicializa os vetores
        for(int i=0; i<Main.problemSize; i++) {
            Main.lBase.add((int)(Math.random()*10)+1);   // base entre 1 e 10
            Main.lExp.add((int)(Math.random()*10));      // expoente entre 0 e 9
            Main.lResp.add(0);             // indica que a resposta ainda não foi calculada
        }

        /*
        System.out.print("Base: ");
        for(int i=0; i<problemSize; i++) {
            System.out.print(lBase.get(i) + " ");
        }
        */

        // usando implementa a ponteciação vetorial usando pool de thread com ExecutorService
        System.out.print(("\nUsando Executor: "));
        long startTime = System.nanoTime();
        usingExecutor();
        long endTime = System.nanoTime();
        printDuration(startTime, endTime);
        checkResult();

        // zera o vetor de respostas
        for(int i=0; i<problemSize; i++) {
            Main.lResp.set(i, 0);
        }
        
        /*
        System.out.print("Base: ");
        for(int i=0; i<problemSize; i++) {
            System.out.print(lBase.get(i) + " ");
        }
        */

        // usando implementa a ponteciação vetorial usando pool de thread criado e gerenciado manualmente
        System.out.print(("\nUsando pool manual: "));
        startTime = System.nanoTime();
        usingManualPool();
        endTime = System.nanoTime();
        printDuration(startTime, endTime);
        checkResult();

        // zera o vetor de respostas
        for(int i=0; i<problemSize; i++) {
            Main.lResp.set(i, 0);
        }

        // usando implementa a ponteciação vetorial usando método sequencial (sem threads)
        System.out.print(("\nUsando método sequencial: "));
        startTime = System.nanoTime();
        usingSequencial();
        endTime = System.nanoTime();
        printDuration(startTime, endTime);
        checkResult();

        System.out.println("\nFinished all threads");
    }

    /**
     * Usando ExecutorService para gerenciar o pool de threads
     */
    private static void usingExecutor() {
        try {
            // cria um pool de threads com tamanho fixo
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            for(int i = 0; i < problemSize; i++) {
                // cria uma nova tarefa (não uma nova thread)
                Runnable worker = new WorkerThread1(i, Main.lBase, Main.lExp, Main.lResp);
                // envia a tarefa para o pool de threads
                // a tarefa somente vai ser executada quando houver uma thread disponível
                executor.execute(worker);
            }
            System.out.println("Todas as tarefas foram enviadas.");
            // encerra o executor
            executor.shutdown();
            // aguarda o término de todas as tarefas em busy-wait
            while (!executor.isTerminated()) {
            }
            /*
            if(!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                System.err.println("Ainda há tarefas em execução após 60 segundos.");
                executor.shutdownNow(); // força o encerramento
                System.err.println("O executor demorou demais para terminar. Forçando o encerramento.");
            }
            */
        } catch (Exception e) {
            System.err.println("A espera foi interrompida.");
            e.printStackTrace();
        }
    }

    /**
     * Criando e gerenciando manualmente o pool de threads
     */
    private static void usingManualPool() {
        // Lista para armazenar as threads
        Thread[] threads = new Thread[poolSize];

        // Criando e iniciando as threads
        for(int i = 0; i < poolSize; i++) {
            WorkerThread2 worker = new WorkerThread2(lBase, lExp, lResp);
            threads[i] = new Thread(worker);
            threads[i].start();
        }

        // Esperando que todas as threads terminem
        for(int i = 0; i < 5; i++) {
            try {
                threads[i].join();  // Espera a thread terminar
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Usando método sequencial (sem threads).
     * 
     * Para problemas pequenos, pode ser mais rápido que usar threads.
     * 
     * @return void
     */
    private static void usingSequencial() {
        for(int i=0; i<lBase.size(); i++) {
            lResp.set(i, MyMath.power(lBase.get(i), lExp.get(i)));
        }
    }
    
    /**
     * Verifica o resultado.
     * 
     * Emite mensagem de OK se tudo estiver correto ou NOK se houver erro.
     * 
     * @return void
     */
    private static void checkResult() {
        boolean error;
        System.out.print("Verificação: ");
        error = false;
        for(int i=0; i<problemSize; i++) {
            if(lResp.get(i) != BigInteger.valueOf(lBase.get(i)).pow((int)lExp.get(i)).longValue()) {
                error = true;
                System.out.println("Erro na posição " + i + " (" + lBase.get(i) + "^" + lExp.get(i) + " = " + lResp.get(i) + ")");
                for(int j=0; j<10; j++) System.out.print(lResp.get(i) + " ");
                System.out.println();
                break;
            } 
        }
        System.out.println(error? "NOK" : "OK");
    }

    /**
     * Imprime a duração entre t0 e t1 em milissegundos
     * 
     * @param t0 tempo inicial em nanossegundos
     * @param t1 tempo final em nanossegundos
     */
    private static void printDuration(long t0, long t1) {
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println("Tempo de execução: " + millis + " ms");
    }
}
