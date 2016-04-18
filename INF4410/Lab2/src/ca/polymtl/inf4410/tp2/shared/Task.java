package ca.polymtl.inf4410.tp2.shared;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Set of calculs
 */
public class Task extends Thread implements Serializable {
	private static final long serialVersionUID = 8447003171795921878L;

	private Master master;

	private List<Calcul> calculs;

	public Task(Master master) {
		this.master = master;
		calculs = new ArrayList<>();
	}

	public void addCalcul(Calcul calcul) {
		calculs.add(calcul);
	}

	public List<Calcul> getCalculs() {
		return calculs;
	}

	@Override
	public void run() {
		if (isEmpty())
			return;

		try {
			boolean success = master.getSlave().sendTask(this);

			// If the slave could not handle this request
			if (!success)
				master.errorOnProcessing();
		} catch (RemoteException e) {
			System.err.println("Slave disconneced while processing.");
			run();
		}
	}

	private boolean isEmpty() {
		for (int i = 0; i < calculs.size(); i++)
			if (!calculs.get(i).isDone())
				return false;
		return true;
	}
}
