package br.com.meslin.rmitls.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Echo extends Remote {
    /**
     * Echoes the given message.
     * @param message the message to echo
     * @return the echoed message
     * @throws RemoteException if a remote communication error occurs
     */
    String echo(String message) throws RemoteException;
}
