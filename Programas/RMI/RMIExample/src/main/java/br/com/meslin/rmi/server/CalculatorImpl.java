package br.com.meslin.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.com.meslin.rmi.shared.Calculator;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

    /**
     * Construtor da classe CalculatorImpl.
     * @param port porta para exportar o objeto remoto
     * @throws RemoteException
     */
    protected CalculatorImpl(int port) throws RemoteException {
        super(port);
    }

    /**
     * Construtor padrão da classe CalculatorImpl.
     * @throws RemoteException
     */
    protected CalculatorImpl() throws RemoteException {
        super();
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.calculator.shared.Calculator#adicionar(double, double)
     */
    @Override
    public double adicionar(double a, double b) throws RemoteException {
        return a + b;
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.calculator.shared.Calculator#subtrair(double, double)
     */
    @Override
    public double subtrair(double a, double b) throws RemoteException {
        return a - b;
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.calculator.shared.Calculator#multiplicar(double, double)
     */
    @Override
    public double multiplicar(double a, double b) throws RemoteException {
        return a * b;
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.calculator.shared.Calculator#dividir(double, double)
     */
    @Override
    public double dividir(double a, double b) throws RemoteException {
        if (b == 0) {
            throw new ArithmeticException("Divisão por zero");
        }
        return a / b;
    }
}
