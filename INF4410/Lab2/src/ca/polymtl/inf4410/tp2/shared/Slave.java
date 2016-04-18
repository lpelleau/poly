package ca.polymtl.inf4410.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Slave extends Remote {
	public boolean sendTask(Task task) throws RemoteException;
}
