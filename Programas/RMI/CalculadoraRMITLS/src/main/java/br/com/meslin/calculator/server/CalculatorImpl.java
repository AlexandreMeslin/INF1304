package br.com.meslin.calculator.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import br.com.meslin.calculator.shared.Calculator;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
    /**
     * Construtor da classe CalculatorImpl com suporte a TLS.
     * @param clientSocketFactory Fábrica de sockets do cliente
     * @param serverSocketFactory Fábrica de sockets do servidor
     * @throws RemoteException
     */
    protected CalculatorImpl(RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws RemoteException {
        super(0, clientSocketFactory, serverSocketFactory);
    }

    /**
     * Construtor da classe CalculatorImpl com suporte a TLS.
     * @param port porta para exportar o objeto remoto
     * @param clientSocketFactory Fábrica de sockets do cliente
     * @param serverSocketFactory Fábrica de sockets do servidor
     * @throws RemoteException
     */
    protected CalculatorImpl(int port, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws RemoteException {
        super(port, clientSocketFactory, serverSocketFactory);
    }

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
