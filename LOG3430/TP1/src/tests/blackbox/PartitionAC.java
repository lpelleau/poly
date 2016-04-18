package tests.blackbox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import implementations.HashTableImpl;
import interfaces.HashTable;

public class PartitionAC {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test chemin de base de données manquant
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop2 – FH1 - MN1 - IC2
	@Test(expected = java.io.FileNotFoundException.class)
	public void testAC1() throws IOException {
		String pathDB = "";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();
	}

	/**
	 * Test de chemin du fichier des operations est null
	 * 
	 * @throws IOException
	 */
	// pdb2 - pop1 – FH1 – MN2 - IC1
	@Test(expected = java.lang.NullPointerException.class)
	public void testAC2() throws IOException {
		String pathDB = null;// "ressources/GestionEtudiants.txt";
		String pathOperations = null;
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => entre 0 et 5 init capacity => <= a 11
	 * 
	 * @throws IOException
	 */
	// pdb1 - pop1 – FH1 - MN1 - IC1
	@Test
	public void testAC3() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();
		table.afficheBD();
		Assert.assertEquals(expected("ressources/AC/ResultTestAC3.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => entre 0 et 5 init capacity => >11 et primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH1 - MN1 - IC2
	@Test
	public void testAC4() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 13;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC4.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => entre 0 et 5 init capacity => >11 et non
	 * primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH1 - MN1 - IC3
	@Test
	public void testAC5() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 24;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC5.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => >5 init capacity => <=11
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH1 - MN2- IC1
	@Test
	public void testAC6() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 6;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC6.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => >5 init capacity => >11 et primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH1 - MN2- IC2
	@Test
	public void testAC7() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 6;
		int initialCapacity = 17;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC7.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * entre 0 et 5 max Node => >5 init capacity => >11 et non primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH1 - MN2- IC3
	@Test
	public void testAC8() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 6;
		int initialCapacity = 25;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC8.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => entre 0 et 5 init capacity => <=11
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN1- IC1
	@Test
	public void testAC9() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC9.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => entre 0 et 5 init capacity => >11 et
	 * primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN1- IC2
	@Test
	public void testAC10() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = 1;
		int initialCapacity = 23;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC10.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => entre 0 et 5 init capacity => >11 et
	 * non primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN1- IC3
	@Test
	public void testAC11() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = 1;
		int initialCapacity = 26;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC11.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => >5 init capacity => <=11
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN2- IC1
	@Test
	public void testAC12() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = 10;
		int initialCapacity = 8;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC12.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => n'est pas entre 0 et 5 init capacity
	 * => >11 et primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN1- IC2
	@Test
	public void testAC13() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = -1;
		int initialCapacity = 29;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC13.txt"), table.toString());
	}

	/**
	 * Test pathDB => valide peration path => valide fonction de hashage =>
	 * n'est pas entre 0 et 5 max Node => n'est pas entre 0 et 5 init capacity
	 * => >11 et non primitive
	 * 
	 * @throws IOException
	 */
	// pdb1 – pop1 – FH2 – MN1- IC3
	@Test
	public void testAC14() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = -1;
		int maxNodes = -1;
		int initialCapacity = 25;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/AC/ResultTestAC14.txt"), table.toString());
	}

	/**
	 * Premet de retourner le contenu d'un fichier
	 * 
	 * @param path
	 *            chemin du fichier
	 * @return le contenu du fichier
	 * @throws IOException
	 */
	private String expected(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		String res = "";
		for (String line : lines) {
			res += line + "\n";
		}

		return res;
	}

}
