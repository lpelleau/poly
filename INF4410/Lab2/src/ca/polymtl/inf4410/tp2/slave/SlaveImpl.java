package ca.polymtl.inf4410.tp2.slave;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;

import ca.polymtl.inf4410.tp2.shared.Calcul;
import ca.polymtl.inf4410.tp2.shared.Master;
import ca.polymtl.inf4410.tp2.shared.Slave;
import ca.polymtl.inf4410.tp2.shared.Task;

/**
 * Server which calculate operations.
 */
public class SlaveImpl extends UnicastRemoteObject implements Slave, Runnable {
	private static final long serialVersionUID = -2919564371631024068L;

	private String address;
	private Master master;
	private boolean run = true;

	private Integer maxOperations;
	private Integer malicious;

	private Random r;
	private static int ID;

	public static void main(String[] args) throws RemoteException {
		new SlaveImpl(args).run();
	}

	public SlaveImpl(String[] args) throws RemoteException {
		r = new Random();
		ID = r.nextInt();
		try {
			maxOperations = Integer.valueOf(args[0]);
			if (maxOperations < 1) {
				System.err.println("Please enter a maximum number of operations (>= 1) and malicious.");
				System.err.println("./slave <max_operations> <malicious> <address>");
				run = false;
			}

			malicious = Integer.valueOf(args[1]);
			if (malicious < 0 || malicious > 100) {
				System.err.println("Please enter a maximum number of operations and malicious (0 <= n <= 100).");
				System.err.println("./slave <max_operations> <malicious> <address>");
				run = false;
			}

			address = args[2];

			System.out.println("I am " + ID + ": maxOp(" + maxOperations + ") & malicious(" + malicious + ")");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please enter a maximum number of operations and malicious.");
			System.err.println("./slave <max_operations> <malicious> <address>");
			run = false;
		} catch (NumberFormatException e) {
			System.err.println("Please enter a maximum number of operations and malicious.");
			System.err.println("./slave <max_operations> <malicious> <address>");
			run = false;
		}
	}

	public void run() {
		if (!run) {
			return;
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String registration = "rmi://" + address + "/Repartiteur";
			Remote remoteService = Naming.lookup(registration);
			master = (Master) remoteService;
			master.taskFinished(this, ID);
		} catch (ConnectException e) {
			System.err.println("Impossible to connect to RMI registry. Is rmiregistry launched ?");
			System.err.println("Error: " + e.getMessage());
		} catch (RemoteException e) {
			System.err.println("Error: " + e.getMessage());
		} catch (NotBoundException e) {
			System.err.println("Error: " + e.getMessage());
		} catch (MalformedURLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Task sent by the master.
	 */
	@Override
	public synchronized boolean sendTask(Task task) throws RemoteException {
		List<Calcul> operations = task.getCalculs();

		// If the ressources can't be allocated
		int u = 0;
		for (int i = 0; i < operations.size(); i++)
			if (!operations.get(i).isDone())
				u++;
		double T = refusalRate(u, maxOperations);
		int g = r.nextInt(100);
		boolean sendRes;
		if (g < T) {
			sendRes = false;
			System.out.println("Task (size=" + u + ") refused from " + ID + " (with T=" + T + " and g=" + g + ").");
		} else {
			sendRes = true;
			System.out.println("Task (size=" + u + ") accepted from " + ID + " (with T=" + T + " and g=" + g + ")).");
		}

		// Perform all calculs
		for (int i = operations.size() - 1; i >= 0; i--) {
			if (operations.get(i).isDone())
				continue;

			Calcul c = operations.get(i);
			Integer result = c.calculate();

			// If this calcul is malicious
			if (r.nextInt(101) < malicious) {
				Integer wrongRes = null;
				// Generate a new one different of the correct calcul
				do {
					wrongRes = r.nextInt();
				} while (wrongRes == result);
				result = wrongRes;
			}

			if (sendRes)
				// Add the result to the Master
				master.addResult(c.getIndexCal(), result, ID);
		}

		master.taskFinished(this, ID);
		return sendRes;
	}

	/**
	 * Calculate if the ressources can be allocated.
	 */
	private double refusalRate(Integer u, Integer q) {
		double res = (u - q) / (9. * q) * 100;
		return (res < 0 ? 0 : res);
	}
}
