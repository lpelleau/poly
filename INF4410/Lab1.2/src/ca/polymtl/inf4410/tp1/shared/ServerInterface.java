package ca.polymtl.inf4410.tp1.shared;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.omg.CORBA.UnknownUserException;

/**
 * Interface shared between both the server and the clients. Represent the API
 * of the server.
 */
public interface ServerInterface extends Remote {
	Integer generateclientid() throws RemoteException;

	boolean create(String name) throws RemoteException;

	String list() throws RemoteException;

	List<FileDir> syncLocalDir() throws RemoteException;

	byte[] get(String name, String checksum) throws RemoteException,
			FileNotFoundException;

	boolean lock(String name, int clientId, String checksum)
			throws RemoteException, FileNotFoundException, UnknownUserException;

	boolean push(String name, byte[] content, int clientId)
			throws RemoteException, FileNotFoundException, UnknownUserException;
}
