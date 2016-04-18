package ca.polymtl.inf4410.tp1.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omg.CORBA.UnknownUserException;

import ca.polymtl.inf4410.tp1.shared.FileDir;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

/**
 * Receive requests from the client.
 */
public class Server implements ServerInterface {
	private Directory dir = new Directory(); // Directory containing the files
												// stored
	private List<Integer> clientsId = new ArrayList<Integer>(); // IDs generates
																// of the
																// clients

	/**
	 * Launch a <i>Server</i> instance.
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	/**
	 * Register the server in RmiRegistery.
	 */
	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err
					.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	/**
	 * Generate unique random ID the a client.
	 */
	public synchronized Integer generateclientid() throws RemoteException {
		Integer r = Math.abs(new Random().nextInt());
		if (clientsId.contains(r)) {
			return generateclientid();
		} else {
			clientsId.add(r);
			return r;
		}
	}

	/**
	 * Add a file in the directory.
	 */
	public synchronized boolean create(String nom) throws RemoteException {
		return dir.createFile(nom);
	}

	/**
	 * Return the listing of the directory.
	 */
	public String list() throws RemoteException {
		return dir.listDir();
	}

	/**
	 * Return all files of the directory.
	 */
	public List<FileDir> syncLocalDir() throws RemoteException {
		return dir.getFiles();
	}

	/**
	 * Return the requested file if checksum in argument is different of the
	 * file in the directory.
	 */
	public byte[] get(String name, String checksum) throws RemoteException,
			FileNotFoundException {
		FileDir f = dir.getFile(name);
		if (f == null) {
			throw new FileNotFoundException();
		}
		if (f.getChecksum() == null) {
			return new byte[0];
		} else if (!f.getChecksum().equals(checksum)) {
			return f.getContent();
		} else {
			return null;
		}
	}

	/**
	 * If the file exist and the file is unlocked, place the client as "locker".
	 * 
	 * @throws UnknownUserException
	 */
	public synchronized boolean lock(String name, int clientId, String checksum)
			throws RemoteException, FileNotFoundException, UnknownUserException {
		FileDir f = dir.getFile(name);
		if (f == null) {
			throw new FileNotFoundException();
		}

		if (!clientsId.contains(clientId)) {
			throw new UnknownUserException();
		}

		if (f.isLocked() && f.getLocker() == clientId) {
			return true;
		} else if (f.isLocked()) {
			return false;
		} else {
			f.setLocker(clientId);
			return true;
		}
	}

	/**
	 * Push the content in the file if the file is locked by the user. The file
	 * is then unlocked.
	 */
	public synchronized boolean push(String name, byte[] content, int clientId)
			throws RemoteException, FileNotFoundException, UnknownUserException {
		FileDir f = dir.getFile(name);
		if (f == null) {
			throw new FileNotFoundException();
		}

		if (!clientsId.contains(clientId)) {
			throw new UnknownUserException();
		}

		if (f.getLocker() == clientId) {
			try {
				f.setContent(content);
				f.setLocker(-1);
				return true;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
