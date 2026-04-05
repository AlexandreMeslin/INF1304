/**
 * Exemplo de uso da interface Runnable para criar threads em Java.
 * <p>
 * Runnable permite separar a tarefa da thread do controle da thread.
 */
package br.com.meslin;

/**
 * Classe que implementa Runnable.
 * <p>
 * Runnable permite separar a tarefa da thread do controle da thread.
 */
public class MinhaRunnable implements Runnable {
    private String nome;

    // Construtor para definir o nome da thread
    public MinhaRunnable(String nome) {
        this.nome = nome;
    }

    /**
     * Método run() que será executado quando a thread iniciar
     */
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(nome + " está executando: " + i);
            try {
                // Pausa a thread por 1 segundo
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(nome + " foi interrompida.");
            }
        }
        System.out.println(nome + " terminou a execução.");
    }
}
