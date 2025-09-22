/**
 * Exemplo de condição de corrida.
 */
package br.com.meslin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exemplo de condição de corrida.
 * 
 * Compile e execute:
 * javac br/com/meslin/Contador.java
 * java br.com.meslin.Contador
 */
public class Contador {
    private final Lock lock = new ReentrantLock();
    private int contador = 0;
    private final static int QTD = 100000000;

    /**
     * Sem sincronismo - condição de corrida!
     */
    public void incrementar_sem_sinc() {
        contador++;
    }
    public void decrementar_sem_sinc() {
        contador--;
    }

    /**
     * Com sincronismo - sem condição de corrida!
     */
    public synchronized void incrementar_com_sinc() {
        contador++;
    }
    public synchronized void decrementar_com_sinc() {
        contador--;
    }

    /**
     * Com sincronismo usando Lock - sem condição de corrida!
     */
    public void incrementar_com_sinc_v2() {
        synchronized(lock) {
            contador++;
        }
    }
    public void decrementar_com_sinc_v2() {
        synchronized(lock) {
            contador--;
        }
    }

    public static void main(String[] args) {
        Thread t1, t2;

        Contador contadorss = new Contador();
        t1 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contadorss.incrementar_sem_sinc();
            }
        });
        t2 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contadorss.decrementar_sem_sinc();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Valor final do contador sem sincronismo: " + contadorss.contador + " (deveria ser 0).");

        Contador contadorcs = new Contador();
        t1 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contadorcs.incrementar_com_sinc();
            }
        });
        t2 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contadorcs.decrementar_com_sinc();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Valor final do contador com métodos sincronizados: " + contadorcs.contador + " (deveria ser 0).");

        Contador contador = new Contador();
        t1 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contador.incrementar_com_sinc();
            }
        });
        t2 = new Thread(() -> {
            for (int i = 0; i < QTD; i++) {
                contador.decrementar_com_sinc();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Valor final do contador com blocos sincronizados: " + contador.contador + " (deveria ser 0).");
    }
}
