package ca.polymtl.inf4410.tp1.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.omg.CORBA.UnknownUserException;

import ca.polymtl.inf4410.tp1.shared.FileDir;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

/**
 * Send requests to the server and interact with the user.
 */
public class Client {
	private static final String ADDR = "127.0.0.1"; // Server address
	private static final String ID_CLIENT_FILE = ".duf"; // Extension of the
															// properties file
	private String[] args; // Arguments seized by the user

	private ServerInterface distantServerStub = null; // Server to talk

	/**
	 * Launch a <i>Client</i> instance.
	 */
	public static void main(String[] args) {
		Client client = new Client(args);
		client.run();
	}

	/**
	 * Constructor which initiate the client with the server.
	 */
	public Client(String[] args) {
		this.args = args;

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		distantServerStub = loadServerStub(ADDR);
	}

	/**
	 * Check if the server is loaded and compute the command seized by the user.
	 */
	private void run() {
		if (distantServerStub != null) {
			dispatchCmd();
		}
	}

	/**
	 * Load the server instance withe the address given.
	 */
	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Error: the name '" + e.getMessage()
					+ "' is not defined in the register.");
		} catch (AccessException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Error: " + e.getMessage());
		}

		return stub;
	}

	/**
	 * Compute the arguments seized by the user. Bad arguments are handled and
	 * display info in the prompt.
	 */
	private void dispatchCmd() {
		try {
			switch (args[0]) {
			case "create":
				callCreate(args[1]);
				break;
			case "list":
				callList();
				break;
			case "syncLocalDir":
				callSyncLocalDir();
				break;
			case "get":
				callGet(args[1]);
				break;
			case "lock":
				callLock(args[1]);
				break;
			case "push":
				callPush(args[1]);
				break;
			case "help":
				callHelp();
				break;
			default:
				System.out.println("Unknown command.");
				callHelp();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				System.out.println("Missing argument to command " + args[0]
						+ ".");
				callHelp();
			} catch (ArrayIndexOutOfBoundsException e1) {
				System.out.println("Missing command.");
				callHelp();
			}
		} catch (RemoteException e) {
			System.out.println("Server connection error: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Impossible to generate checksum: "
					+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Impossible to read/write local file: "
					+ e.getMessage());
		}
	}

	/**
	 * Return the content of the file given in parameter.
	 */
	private byte[] getFileContent(String name) throws IOException {
		return Files.readAllBytes(Paths.get(name));
	}

	/**
	 * Retrieve the client id from the properties file. If the file is not
	 * found, the server is called to generate a new id and store it in the
	 * file.
	 */
	private int getClientId() throws RemoteException {
		try {
			List<String> f = Files.readAllLines(Paths.get(ID_CLIENT_FILE));
			return Integer.valueOf(f.get(0));
		} catch (IOException e) {
			callGenerateclientid();
			return getClientId();
		} catch (Exception e) {
			throw new NumberFormatException();
		}
	}

	/**
	 * Write the client id in the properties file.
	 */
	private void setClId(Integer id) {
		try {
			PrintWriter writer = new PrintWriter(ID_CLIENT_FILE, "UTF-8");
			writer.println(id);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call the server.
	 */
	private void callGenerateclientid() throws RemoteException {
		Integer clId = distantServerStub.generateclientid();
		setClId(clId);
	}

	/**
	 * Call the server to create a file with the given name.
	 */
	private void callCreate(String name) throws RemoteException {
		if (distantServerStub.create(name)) {
			System.out.println("File " + name + " added.");
		} else {
			System.out.println("Error at file creation, name: " + name);
		}
	}

	/**
	 * Ask the server for the list of files presents.
	 */
	private void callList() throws RemoteException {
		System.out.println(distantServerStub.list());
	}

	/**
	 * Synchronize the current repository with the files presents on the server.
	 */
	private void callSyncLocalDir() throws RemoteException {
		List<FileDir> files = distantServerStub.syncLocalDir();
		for (FileDir f : files) {
			try {
				FileOutputStream fos = new FileOutputStream(f.getName());
				if (f.getContent() != null) {
					fos.write(f.getContent());
				}
				fos.close();
			} catch (FileNotFoundException e) {
				System.out.println("Impossible to write file " + f.getName());
			} catch (IOException e) {
				System.out.println("Impossible to write file " + f.getName());
			}
		}
		System.out.println("Files are synchronized !");
	}

	/**
	 * Retrieve a specific file from the server without prompt. Doesn't download
	 * the content if local content is identic
	 */
	private void callGet(String name) throws NoSuchAlgorithmException,
			IOException {
		callGet(name, false);
	}

	/**
	 * Retrieve a specific file from the server with prompt. Doesn't download
	 * the content if local content is identic
	 */
	private void callGet(String name, boolean silent)
			throws NoSuchAlgorithmException, IOException {
		String checksum = null;
		try {
			FileDir f = new FileDir(name);
			f.setContent(Files.readAllBytes(Paths.get(name)));
			checksum = f.getChecksum();
		} catch (IOException e) {
			checksum = "-1";
		}

		byte[] content = null;
		try {
			content = distantServerStub.get(name, checksum);
		} catch (FileNotFoundException e) {
			if (!silent) {
				System.out.println("The file " + name
						+ " doesn't exist on the server");
				System.out.println("Please create the file before.");
			}
			return;
		}

		if (content == null) {
			if (!silent) {
				System.out.println("File already up-to-date.");
			}
		} else {
			FileOutputStream fos = new FileOutputStream(name);
			fos.write(content);
			fos.close();
		}
	}

	/**
	 * Lock a specific file in the server from write from others users.
	 */
	private void callLock(String name) throws NoSuchAlgorithmException,
			IOException {
		callGet(name, true);
		FileDir f = new FileDir(name);
		f.setContent(Files.readAllBytes(Paths.get(name)));
		;
		String checksum = f.getChecksum();
		try {
			if (distantServerStub.lock(name, getClientId(), checksum)) {
				System.out.println("File successfully locked.");
				System.out.println("The file is locked for only three (3) minutes.");
				System.out.println("After this time, the file will be unlocked.");
				System.out.println("Ilaced with the server content.");
			} else {
				System.out.println("File already locked by another user.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("The file " + name
					+ " doesn't exist on the server");
			System.out.println("Please create the file before.");
		} catch (UnknownUserException e) {
			System.out.println("Your client ID doesn't exist on the server.");
			if (new File(ID_CLIENT_FILE).delete()) {
				System.out.println("The file containing your ID was deleted.");
				System.out.println("A new one will be generated.");
			} else {
				System.out.println("The file " + ID_CLIENT_FILE
						+ " containing your ID couldn't be deleted.");
				System.out.println("Please delete it manually.");
			}
		}
	}

	/**
	 * Push the content of a file on the server. Only available if the file is
	 * previously locked.
	 */
	private void callPush(String name) throws RemoteException, IOException {
		try {
			if (distantServerStub.push(name, getFileContent(name),
					getClientId())) {
				System.out.println("File " + name + " updated.");
			} else {
				System.out
						.println("Please lock your file before push modifications.");
			}
		} catch (NumberFormatException e) {
			System.out.println("The " + ID_CLIENT_FILE + " file is corrupted.");
		} catch (FileNotFoundException e) {
			System.out.println("The file " + name
					+ " doesn't exist on the server");
			System.out.println("Please create the file before.");
		} catch (UnknownUserException e) {
			System.out.println("Your client ID doesn't exist on the server");
			if (new File(ID_CLIENT_FILE).delete()) {
				System.out.println("The file containing your ID was deleted.");
				System.out.println("A new one will be generated.");
			} else {
				System.out.println("The file " + ID_CLIENT_FILE
						+ " containing your ID couldn't be deleted.");
				System.out.println("Please delete it manually.");
			}
		}
	}

	/**
	 * Display help to the user.
	 */
	private void callHelp() {
		System.out.println("Commands availables:");
		System.out.println("\t- create <file_name>");
		System.out.println("\t- list");
		System.out.println("\t- syncLocalDir");
		System.out.println("\t- get <file_name>");
		System.out.println("\t- lock <file_name>");
		System.out.println("\t- push <file_name>");
		System.out.println("\t- help");
		System.out.println("Enjoy !");
	}
}
