package ca.polymtl.inf4410.tp2.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.polymtl.inf4410.tp2.slave.Operations;

/**
 * Define a calcul with an operator (either FIB or PRIME) and an operande.
 */
public class Calcul implements Serializable {
	private static final long serialVersionUID = 8447003171795921878L;

	private Integer indexCal;

	private Operator operator;
	private Integer operande;
	private List<Integer> results;
	private boolean done;

	public Calcul(Integer indexCal, Operator operator, Integer operande) {
		this.indexCal = indexCal;
		this.operator = operator;
		this.operande = operande;
		results = new ArrayList<>();
		done = false;
	}

	/**
	 * Perform the calcul. Return the result but don't store it in object
	 * variable.
	 */
	public Integer calculate() {
		Integer res = null;
		switch (operator) {
		case FIB:
			res = Operations.fib(operande);
			break;
		case PRIME:
			res = Operations.prime(operande);
			break;
		}
		return res;
	}

	/**
	 * Return the result. Null if not already performed.
	 */
	public List<Integer> getResults() {
		return results;
	}

	public void addResults(Integer result) {
		results.add(result);
	}

	public Integer getIndexCal() {
		return indexCal;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone() {
		done = true;
	}

	@Override
	public String toString() {
		return operator.toString() + " " + operande;
	}

	public enum Operator {
		FIB, PRIME
	}
}
