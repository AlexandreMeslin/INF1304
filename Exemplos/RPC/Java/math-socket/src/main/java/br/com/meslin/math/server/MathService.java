/**
 * @author Meslin
 * @file MathService.java
 * @brief Interface de serviço matemático para RPC
 * Definição dos métodos remotos disponíveis.
 */
//package br.com.meslin.math.commun;

/**
 * Interface de serviço matemático
 * Define os métodos remotos disponíveis.
 */
public interface MathService {
    /**
     * Soma dois inteiros
     * @param a Primeiro inteiro
     * @param b Segundo inteiro
     * @return Soma de a e b
     */
    int soma(int a, int b);
    
    /**
     * Multiplica dois inteiros
     * @param a Primeiro inteiro
     * @param b Segundo inteiro
     * @return Produto de a e b
     */
    int multiplica(int a, int b);
}
