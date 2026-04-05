package br.com.meslin.calculator.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    double adicionar(double a, double b) throws RemoteException;
    double subtrair(double a, double b) throws RemoteException;
    double multiplicar(double a, double b) throws RemoteException;
    double dividir(double a, double b) throws RemoteException;
}
