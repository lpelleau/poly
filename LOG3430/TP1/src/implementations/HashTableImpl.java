
package implementations;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import interfaces.HashTable;
import interfaces.Map;
import interfaces.Map.Element;

/**
 * Contain a HashMap to store students. <br />
 * Parameters given in the parameter are controlled to correspond to the needs
 * and don't have incorrect value.
 */
public class HashTableImpl implements HashTable {
	private static final int MAX_NODE = 5;
	private static final int INITIAL_CAPACITY = 11;

	private String pathDB;
	private String pathOperation;
	private int fonctHashage;
	private int maxNodes;
	private int initialCapacity;

	private Map etudiants;

	public HashTableImpl(String pathDB, String pathOperation, int fonctHashage, int maxNodes, int initialCapacity)
			throws IOException {
		this.pathDB = pathDB;
		this.pathOperation = pathOperation;
		this.fonctHashage = (fonctHashage != 1 && fonctHashage != 2) ? 1 : fonctHashage;
		this.maxNodes = (maxNodes > 5) ? MAX_NODE : maxNodes;
		this.maxNodes = (this.maxNodes <= 0) ? 1 : this.maxNodes;
		this.initialCapacity = (initialCapacity < INITIAL_CAPACITY) ? INITIAL_CAPACITY
				: ((!HashMap.isPrime(initialCapacity)) ? HashMap.nextPrime(initialCapacity) : initialCapacity);

		etudiants = new HashMap(this.fonctHashage, this.maxNodes, this.initialCapacity);
	}

	/* (non-Javadoc)
	 * @see implementations.HashTable#chargerFichier()
	 */
	@Override
	public void chargerFichier() throws IOException {
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(pathDB), "UTF-8");
			// Reader reader = new FileReader(pathDB);

			StringBuilder studentId = null;
			StringBuilder studentName = null;
			Element e = null;

			int data = reader.read();
			
			while (data != -1) {
				char dataChar = (char) data;

				if (dataChar == '<') {
					e = new Element();
					studentId = new StringBuilder();

				} else if (dataChar == ',') {
					e.setId(Integer.valueOf(studentId.toString()));
					studentId = null;
					studentName = new StringBuilder();

				} else if (dataChar == '>') {
					e.setName(studentName.toString());
					studentName = null;
					etudiants.put(e.getId(), e);
					e = null;

				} else {
					if (studentId != null) {
						studentId.append(dataChar);
					} else if (studentName != null) {
						studentName.append(dataChar);
					}
				}

				data = reader.read();
			}

			reader.close();
		} catch (NullPointerException e) {
			throw new NullPointerException("Null exception");
		}
	}

	/* (non-Javadoc)
	 * @see implementations.HashTable#executerOperations()
	 */
	@Override
	public void executerOperations() throws IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(pathOperation), "UTF-8");
		// Reader reader = new FileReader(pathOperation);

		Mode mode = null;
		StringBuilder studentId = null;
		StringBuilder studentName = null;
		Element e = null;

		int data = reader.read();
		if (data == -1) {
			reader.close();
			throw new IOException();
		}
		while (data != -1) {
			char dataChar = (char) data;

			if (dataChar == Mode.Add.letter) {
				mode = Mode.Add;

			} else if (dataChar == Mode.Delete.letter) {
				mode = Mode.Delete;

			} else if (dataChar == Mode.Erease.letter) {
				mode = Mode.Erease;
				etudiants.clear();
				mode = Mode.Add;

			} else if (dataChar == '<') {
				e = new Element();
				studentId = new StringBuilder();

			} else if (dataChar == ',') {
				e.setId(Integer.valueOf(studentId.toString()));
				studentId = null;
				studentName = new StringBuilder();

			} else if (dataChar == '>') {
				if (studentName != null) {
					e.setName(studentName.toString());
					studentName = null;
				} else {
					e.setId(Integer.valueOf(studentId.toString()));
					studentId = null;
				}
				switch (mode) {
				case Add:
					etudiants.put(e.getId(), e);
					break;
				case Delete:
					etudiants.remove(e.getId());
					break;
				case Erease:
					// Never go here
					break;
				}
				e = null;

			} else {
				if (studentId != null) {
					studentId.append(dataChar);
				} else if (studentName != null) {
					studentName.append(dataChar);
				}
			}

			data = reader.read();
		}

		reader.close();
	}

	/* (non-Javadoc)
	 * @see implementations.HashTable#sauvegarderBD()
	 */
	@Override
	public void sauvegarderBD() throws IOException {
		Writer writer = new FileWriter(pathDB);
		writer.write(etudiants.toString());
		writer.close();
	}

	/* (non-Javadoc)
	 * @see implementations.HashTable#afficheBD()
	 */
	@Override
	public void afficheBD() {
		System.out.println(toString());
	}

	/**
	 * Return the content of the HashMap in a String.
	 */
	@Override
	public String toString() {
		return etudiants.toString();
	}

	/**
	 * Different available operations. <br />
	 * Change the letter to read in the file here.
	 */
	private enum Mode {
		Add('A'), Delete('S'), Erease('E');

		private char letter;

		private Mode(char letter) {
			this.letter = letter;
		}
	}
}