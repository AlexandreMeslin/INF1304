/**
 * Exemplo de uso de thread com a classe Thread
 * Para compilar:
 * - $ mvn clean package
 * Para executar:
 * - $ java -cp target/exemplo-thread-1.0-SNAPSHOT.jar br.com.meslin.ExemploThread
 * - $ java -jar target/exemplo-thread-1.0-SNAPSHOT.jar
 */
package br.com.meslin;

/** Classe MinhaThread estende Thread */
class MinhaThread extends Thread {
    private String nome;

    /**
     * Constructor
     * @param nome nome da thread
     */
    public MinhaThread(String nome) {
        this.nome = nome;
    }

    /**
     * Método run
     * Este método executa quando a thread iniciar
     */
    @Override
    public void run() {
        for(int i = 0; i < 5; i++) {
            System.out.println(nome + " está executando: " + i);
            try {
                // Pausa a thread por 1 segundo
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.out.println(nome + " foi interrompida.");
            }
        }
        System.out.println(nome + " terminou a execução.");
    }
}

// Classe principal para iniciar as threads

/**
 * Classe ExemploThread.
 * Classe principal.
 * Essa classe inicia as threads.
 */
public class ExemploThread {
    public static void main(String[] args) {
        // Criação das threads
        MinhaThread thread1 = new MinhaThread("Thread 1");
        MinhaThread thread2 = new MinhaThread("Thread 2");

        // Iniciar as threads (não chame o método run diretamente!)
        thread1.start();
        thread2.start();
        System.out.println("Método main terminou.");
    }
}
