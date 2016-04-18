package ca.polymtl.inf4410.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Master extends Remote {
	void addResult(Integer indexCal, Integer result, Integer idSlave) throws RemoteException;

	void taskFinished(Slave slave, Integer idSlave) throws RemoteException;

	Slave getSlave() throws RemoteException;

	void errorOnProcessing() throws RemoteException;
}
