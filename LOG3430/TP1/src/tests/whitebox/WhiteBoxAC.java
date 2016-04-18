package tests.whitebox;

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

import implementations.HashMap;
import implementations.HashTableImpl;
import interfaces.HashTable;
import interfaces.Map;
import interfaces.Map.Element;

public class WhiteBoxAC {

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
	public void testElementClass1() {

		Element elem1 = new Map.Element();
		elem1.setId(0);
		elem1.setName("elem");
		Element elem2 = new Map.Element();
		elem2.setId(0);
		elem2.setName("elem");
		Assert.assertTrue(elem1.equals(elem2));
	}

	@Test
	public void testElementClass2() {
		Element elem1 = new Map.Element(0, "elem");
		Element elem2 = new Map.Element(1, "elem");
		Assert.assertFalse(elem1.equals(elem2));

	}

	@Test
	public void testHashMapClass1() throws IOException {
		HashMap hm = new HashMap(1, 1, 11);
		Element elem1 = new Map.Element(1664321, "Mackly Férère-Antoine");
		Element elem2 = new Map.Element(1281422, "Mackly Férère-Antoine");
		hm.put(elem1.getId(), elem1);
		hm.put(elem2.getId(), elem2);

		Assert.assertTrue(elem1.toString() != elem2.toString());
	}

	@Test
	public void testHashMapClass2() throws IOException {
		HashMap hm = new HashMap(1, 1, 11);
		Element elem1 = new Map.Element(1664321, "Mackly Férère-Antoine");
		Element elem2 = new Map.Element(1281422, "Mackly Férère-Antoine");
		Element elem3 = new Map.Element(1674501, "Mackly Férère-Antoine");
		Element elem4 = new Map.Element(1025635, "Mackly Férère-Antoine");
		hm.put(elem1.getId(), elem1);
		hm.put(elem2.getId(), elem2);
		hm.put(elem3.getId(), elem3);
		hm.put(elem4.getId(), elem4);

		Assert.assertEquals(expected("ressources/WhiteBox/ResultatWithTestHashMap2.txt"), hm.toString());
	}

	@Test
	public void testHashMapClass3() throws IOException {
		HashMap hm = new HashMap(1, 1, 11);
		Element elem1 = new Map.Element(1664321, "Mackly Férère-Antoine");
		Element elem2 = new Map.Element(1281422, "Mackly Férère-Antoine");
		Element elem3 = new Map.Element(1674501, "Mackly Férère-Antoine");
		Element elem4 = new Map.Element(1025635, "Mackly Férère-Antoine");
		hm.put(elem1.getId(), elem1);
		hm.put(elem2.getId(), elem2);
		hm.put(elem3.getId(), elem3);
		hm.put(elem4.getId(), elem4);

		Element elementWillBeRemoved = hm.get(1674501);
		Element removedElement = hm.remove(1674501);
		System.out.println(hm.size());

		Assert.assertTrue(hm.size() == 4 && elementWillBeRemoved.equals(removedElement));
	}

	@Test
	public void testHashMapClass4() throws IOException {
		HashMap hm = new HashMap(1, 1, 11);
		// <1664321,Mackly Férère-Antoine><1281422,Mackly Férère-Antoine>
		Element elem1 = new Map.Element(1664321, "Mackly Férère-Antoine");
		Element elem2 = new Map.Element(1281422, "Mackly Férère-Antoine");
		Element elem3 = new Map.Element(1674501, "Mackly Férère-Antoine");
		Element elem4 = new Map.Element(1025635, "Mackly Férère-Antoine");
		hm.put(elem1.getId(), elem1);
		hm.put(elem2.getId(), elem2);
		hm.put(elem3.getId(), elem3);
		hm.put(elem4.getId(), elem4);
		hm.put(elem1.getId(), elem1);

		Element elementWillBeRemoved = hm.get(1281422);
		Element removedElement = hm.remove(1281422);
		System.out.println(hm.size());

		Assert.assertTrue(hm.size() == 5 && elementWillBeRemoved.equals(removedElement));
	}

	@Test
	public void testHashMapClass5() throws IOException {
		String pathDB = "ressources/GestionEtudiants.txt";
		String pathOperations = "ressources/Operations.txt";
		int fonctHashage = 21;
		int maxNodes = 1;
		int initialCapacity = 11;

		HashTable table = new HashTableImpl(pathDB, pathOperations, fonctHashage, maxNodes, initialCapacity);
		table.chargerFichier();
		table.executerOperations();
		table.afficheBD();
		table.sauvegarderBD();
		Assert.assertEquals(expected("ressources/AC/ResultTestAC3.txt"), table.toString());
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
