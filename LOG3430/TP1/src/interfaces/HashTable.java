package interfaces;

import java.io.IOException;

public interface HashTable {

	/**
	 * Load the DB file and put the students constructed in the HashMap.
	 */
	public abstract void chargerFichier() throws IOException;

	/**
	 * Read the operations file and execute the operations read on the HashMap.
	 */
	public abstract void executerOperations() throws IOException;

	/**
	 * Write the new content of the HashMap in the DB file.
	 */
	public abstract void sauvegarderBD() throws IOException;

	/**
	 * Display the content of the HashMap in the prompt.
	 */
	public abstract void afficheBD();

}