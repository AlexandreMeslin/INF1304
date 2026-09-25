package br.com.meslin.rmitls.server;

import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import br.com.meslin.rmitls.shared.Echo;

public class EchoImpl extends UnicastRemoteObject implements Echo {
    /**
     * Construtor da classe EchoImpl.
     * @throws java.rmi.RemoteException
     */
    protected EchoImpl() throws java.rmi.RemoteException {
        super();
    }

    /**
     * Construtor da classe EchoImpl com suporte a TLS.
     * @param clientSocketFactory Fábrica de sockets do cliente
     * @param serverSocketFactory Fábrica de sockets do servidor
     * @throws java.rmi.RemoteException
     */
    protected EchoImpl(RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws java.rmi.RemoteException {
        super(0, clientSocketFactory, serverSocketFactory);
    }

    /**
     * Construtor da classe EchoImpl com suporte a TLS.
     * @param port porta para exportar o objeto remoto
     * @param clientSocketFactory Fábrica de sockets do cliente
     * @param serverSocketFactory Fábrica de sockets do servidor
     * @throws java.rmi.RemoteException
     */
    protected EchoImpl(int port, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory) throws java.rmi.RemoteException {
        super(port, clientSocketFactory, serverSocketFactory);
    }

    /**    (non-Javadoc)
     * 
     * @see br.com.meslin.rmitls.shared.Echo#echo(java.lang.String)
     */
    @Override
    public String echo(String message) throws java.rmi.RemoteException {
        return message;
    }
}
