package ca.polymtl.inf4410.tp2.master;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.polymtl.inf4410.tp2.shared.Calcul;
import ca.polymtl.inf4410.tp2.shared.Calcul.Operator;
import ca.polymtl.inf4410.tp2.shared.Master;
import ca.polymtl.inf4410.tp2.shared.Slave;
import ca.polymtl.inf4410.tp2.shared.Task;

/**
 * Entry point of the system. Give work to the servers.
 */
public class MasterImpl extends UnicastRemoteObject implements Master, Runnable {
	private static final long serialVersionUID = 3428351189999377341L;

	private String fileToParse;
	private Mode mode;
	private boolean run = true;

	private String address;
	private LinkedList<Slave> slavesFIFO;

	private List<Calcul> calculs;
	private Integer finalResult = 0;
	private Integer nbResults = 0;

	private Integer sizeTask = 10;
	private Integer nbErrorsOnSize;

	private long startTime;
	private long stopTime;

	/**
	 * Launch the Master server with file path as argument.
	 * 
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		new MasterImpl(args).run();
	}

	/**
	 * Check if the file given exist. If there is a second argument, the server
	 * switch to unsecured mode.
	 */
	public MasterImpl(String[] args) throws RemoteException {
		try {
			fileToParse = args[0];
			if (!new File(fileToParse).isFile()) {
				throw new FileNotFoundException();
			}

			address = args[1];

			try {
				String o = args[2];
				if (o.equals("unsecured"))
					mode = Mode.UNSECURED;
				else
					mode = Mode.SECURED;
			} catch (ArrayIndexOutOfBoundsException e) {
				mode = Mode.SECURED;
			}

			calculs = new ArrayList<>();
			slavesFIFO = new LinkedList<>();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please enter a file name for calculation.");
			System.err.println("./master <file_name> <address> [unsecured]");
			run = false;
		} catch (FileNotFoundException e) {
			System.err.println("The file \"" + fileToParse + "\" was not found.");
			System.err.println("Abort.");
			run = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse the file and send tasks to slave servers.
	 */
	public void run() {
		if (!run) {
			return;
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String registration = "rmi://" + address + "/Repartiteur";
			Naming.rebind(registration, this);

			System.out.println("Master ready.");

			parseFile();

			// Wait 10sc, the time for the slaves to connect to the master
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(calculs.size() + " calculs to solve.");
			System.out.println("Begin calculation in " + mode.toString() + " mode.");

			calculate();

		} catch (ConnectException e) {
			System.err.println("Impossible to connect to RMI registry. Is rmiregistry launched ?");
			System.err.println("Error: " + e.getMessage());
		} catch (RemoteException e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			System.err.println("Error: " + e1.getMessage());
			e1.printStackTrace();
		}
	}

	/**
	 * Parse the data file and add the calculs in our list.
	 */
	private void parseFile() {
		try {
			Pattern p = Pattern.compile("([a-z]+) ([0-9]+)");

			List<String> lines = Files.readAllLines(Paths.get(fileToParse), Charset.forName("UTF-8"));
			int i = 0;
			for (String line : lines) {
				Matcher m = p.matcher(line);

				if (m.matches()) {
					try {
						String operator = m.group(1);
						int operand = Integer.valueOf(m.group(2));

						if (operator.equals("fib")) {
							calculs.add(new Calcul(calculs.size(), Operator.FIB, operand));
						} else if (operator.equals("prime")) {
							calculs.add(new Calcul(calculs.size(), Operator.PRIME, operand));
						}
					} catch (IllegalStateException e) {
						System.err.println("Line " + i + " has bad format. Skipped.");
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("Line " + i + " has bad format. Skipped.");
					}
				} else {
					System.err.println("Line " + i + " has bad format. Skipped.");
				}
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method of this class: perform the calculation.
	 */
	private void calculate() {
		startTime = System.currentTimeMillis();

		// While all calculs aren't solved
		while (nbResults != calculs.size()) {
			// Generation of new tasks (set of calculs)
			List<Task> tasks = generateTasks();

			nbErrorsOnSize = 0;

			// Creation of threads to execute tasks
			for (int i = 0; i < tasks.size(); i++) {
				tasks.get(i).start();
			}

			// Wait for all threads to finish
			for (int i = 0; i < tasks.size(); i++) {
				try {
					tasks.get(i).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			redifineSizeTask();
		}
		System.out.println("The final result is: " + finalResult);
		stopTime = System.currentTimeMillis();
		System.out.println("Work done in: " + (stopTime - startTime) + "ms.");
	}

	/**
	 * Generate tasks from the calculs based of the number of calculs per tasks
	 */
	private List<Task> generateTasks() {
		List<Task> tasks = new ArrayList<>();
		int j = -1;
		int n = 0;

		for (int i = 0; i < calculs.size(); i++) {
			// Last task full, creation of a new one
			if (n % sizeTask == 0) {
				tasks.add(new Task(this));
				j++;
			}

			// Only add not completed calculs
			if (!calculs.get(i).isDone()) {
				tasks.get(j).addCalcul(calculs.get(i));
				n++;
			}
		}

		return tasks;
	}

	public void errorOnProcessing() {
		nbErrorsOnSize++;
	}

	/**
	 * Decrements the size of a task if needed and possible
	 */
	private void redifineSizeTask() {
		// Good result, no need to redifine sizeTask
		if (nbResults == calculs.size())
			return;
		// Number of error acceptable (less than 20%)
		if (nbErrorsOnSize < slavesFIFO.size() / 5)
			return;
		// Decrease the number of calculs in a task
		if (sizeTask > 1)
			sizeTask--;
	}

	/**
	 * Return the id of a free slave
	 */
	public Slave getSlave() {
		Slave slave = null;

		while (slave == null) {
			try {
				synchronized (slavesFIFO) {
					slave = slavesFIFO.removeFirst();
				}
			} catch (NoSuchElementException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
				return getSlave();
			}
		}

		return slave;
	}

	/**
	 * Add the result provided by a slave to the final result
	 */
	@Override
	public synchronized void addResult(Integer indexCal, Integer result, Integer idSlave) throws RemoteException {
		// Add the result to it's specific Calcul object
		List<Integer> results = calculs.get(indexCal).getResults();
		results.add(result);

		System.out.println("Result n°" + indexCal + ", `" + calculs.get(indexCal).toString() + "`: " + result);

		if (mode == Mode.UNSECURED) {
			if (calculs.get(indexCal).isDone())
				return;

			// More than one result
			if (results.size() < 2)
				return;

			// Check if more than 50% of the results are identical
			int same = 0;
			for (int i = 0; i < results.size() - 1; i++) {
				if (results.get(i).equals(result))
					same++;
			}

			if (same < results.size() / 2)
				return;
		}

		System.out.print("\t-> added! (by " + idSlave + ")");

		// Set calcul done and add result to finalResult
		calculs.get(indexCal).setDone();
		finalResult += result;
		finalResult %= 5000;
		nbResults++;

		System.out.println(" (" + nbResults + "/" + calculs.size() + ")");
	}

	@Override
	public void taskFinished(Slave slave, Integer idSlave) throws RemoteException {
		System.out.println("Slave n°" + idSlave + " added to FIFO");
		slavesFIFO.add(slave);
	}

	private enum Mode {
		SECURED, UNSECURED
	}
}
