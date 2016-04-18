package tests.blackbox;

import java.io.FileNotFoundException;
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

public class PartitionEC {

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

	@Test
	public void test1() throws IOException { // All valid inputs
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/EC/ResultTestEC1.txt"), table.toString());
	}

	@Test(expected = FileNotFoundException.class)
	public void test2() throws IOException {
		String pathDB = "ressources/DONT_EXIST.txt"; // Invalid input
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 4;
		int initialCapacity = 13;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();

		Assert.fail("File shouldn't be found");

		table.executerOperations();
	}

	@Test(expected = NullPointerException.class)
	public void test3() throws IOException {
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = null; // Invalid input
		int fonctHashage = 1;
		int maxNodes = 4;
		int initialCapacity = 13;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.fail("File shouldn't be found");
	}

	@Test
	public void test4() throws IOException {
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 5; // Invalid input
		int maxNodes = 2;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/EC/ResultTestEC4.txt"), table.toString());
	}

	@Test
	public void test5() throws IOException {
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 2;
		int maxNodes = 12; // Invalid input
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/EC/ResultTestEC5.txt"), table.toString());
	}

	@Test
	public void test6() throws IOException {
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 1;
		int maxNodes = 5;
		int initialCapacity = 400; // Invalid input

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();

		Assert.assertEquals(expected("ressources/EC/ResultTestEC6.txt"), table.toString());
	}

	private String expected(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
		String res = "";
		for (String line : lines) {
			res += line + "\n";
		}
		return res;
	}

}
