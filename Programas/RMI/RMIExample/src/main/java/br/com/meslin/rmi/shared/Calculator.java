package br.com.meslin.rmi.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    /**
     * Adiciona dois números.
     * @param a o primeiro número
     * @param b o segundo número
     * @return a soma dos dois números
     * @throws RemoteException
     */
    double adicionar(double a, double b) throws RemoteException;

    /**
     * Subtrai o segundo número do primeiro.
     * @param a o primeiro número
     * @param b o segundo número
     * @return a diferença entre os dois números
     * @throws RemoteException
     */
    double subtrair(double a, double b) throws RemoteException;

    /**
     * Multiplica dois números.
     * @param a o primeiro número
     * @param b o segundo número
     * @return o produto dos dois números
     * @throws RemoteException
     */
    double multiplicar(double a, double b) throws RemoteException;

    /**
     * Divide o primeiro número pelo segundo.
     * @param a o primeiro número
     * @param b o segundo número
     * @return o quociente dos dois números
     * @throws RemoteException
     */
    double dividir(double a, double b) throws RemoteException;
}
