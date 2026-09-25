package br.com.meslin.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.com.meslin.rmi.shared.Echo;

public class EchoImpl extends UnicastRemoteObject implements Echo {
    /**
     * Construtor da classe EchoImpl.
     * @throws java.rmi.RemoteException
     */
    protected EchoImpl() throws RemoteException {
        super();
    }

    /**
     * Construtor da classe EchoImpl
     * @param port porta para exportar o objeto remoto
     * @param clientSocketFactory Fábrica de sockets do cliente
     * @param serverSocketFactory Fábrica de sockets do servidor
     * @throws java.rmi.RemoteException
     */
    protected EchoImpl(int port) throws java.rmi.RemoteException {
        super(port);
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.rmi.shared.Echo#echo(java.lang.String)
     */
    @Override
    public String echo(String message) throws java.rmi.RemoteException {
        return message;
    }
}
