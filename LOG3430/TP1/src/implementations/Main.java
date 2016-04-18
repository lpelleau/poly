package implementations;

import java.io.IOException;



/**
 * Main used to execute the project with files presents in the ressource folder.
 * <br />
 * The hashage function, max node and initial capacity can be send with argument
 * to the main. <br />
 * If not, default value will be used. <br />
 * The main load the DB, display on the prompt, execute the operations and
 * display the new DB.
 */
public class Main {
	public static void main(String argv[]) throws IOException {
		int fonctHashage, maxNode, initialCapacity;

		try {
			fonctHashage = Integer.valueOf(argv[0]);
		} catch (Exception e) {
			fonctHashage = 1;
		}
		try {
			maxNode = Integer.valueOf(argv[1]);
		} catch (Exception e) {
			maxNode = 1;
		}
		try {
			initialCapacity = Integer.valueOf(argv[2]);
		} catch (Exception e) {
			initialCapacity = 24;
		}
		

		HashTable table = new HashTable("ressources/GestionEtudiants.txt", "ressources/GestionEtudiants.txt", fonctHashage,
				maxNode, initialCapacity);
		try {
			table.chargerFichier();
			table.afficheBD();
			table.executerOperations();
			System.out.println("===");
			table.afficheBD();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
