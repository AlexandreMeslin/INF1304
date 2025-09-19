/**
 * Exemplo de uso da interface Runnable para criar threads em Java.
 * 
 * Para compilar:
 * - $ mvn clean package
 * Para executar:
 * - $ java -cp target/exemplo-runnable-1.0-SNAPSHOT.jar br.com.meslin.ExemploRunnable
 * - $ java -jar target/exemplo-runnable-1.0-SNAPSHOT.jar
 */
package br.com.meslin;

/**
 * Classe MinhaRunnable implementa Runnable
 * Cada instância dessa classe representa uma tarefa que pode ser executada em uma thread.
 * O método run() contém o código que será executado quando a thread iniciar.
 * A implementação de Runnable é preferida quando a classe já estende outra classe,
 * pois Java não suporta herança múltipla.
 */
public class ExemploRunnable {
    public static void main(String[] args) {
        System.out.println("Iniciando threads...");

        // Criação das tarefas Runnable
        Runnable tarefa1 = new MinhaRunnable("Thread 1");
        Runnable tarefa2 = new MinhaRunnable("Thread 2");

        // Criação e início das threads
        Thread thread1 = new Thread(tarefa1);
        Thread thread2 = new Thread(tarefa2);

        thread1.start();
        thread2.start();

        System.out.println("Método main terminou.");
    }
}
