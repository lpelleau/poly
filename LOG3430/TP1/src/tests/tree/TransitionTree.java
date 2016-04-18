package tests.tree;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import implementations.HashMap;
import interfaces.Map;
import interfaces.Map.Element;

public class TransitionTree {

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
	public void nonInitInitializationVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());
	}

	@Test
	public void videRemoveVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.remove(4567);

		Assert.assertEquals(0, map.size());
	}

	@Test
	public void videClearVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.clear();

		Assert.assertEquals(0, map.size());
	}

	@SuppressWarnings("unused")
	@Test
	public void videSizeGetVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		Element e = map.get(4567);
		int n = map.size();

		Assert.assertEquals(0, map.size());
	}

	@Test
	public void videPutNonVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());
	}

	@Test
	public void nonVideRemoveVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());

		map.remove(4567);

		Assert.assertEquals(0, map.size());
	}

	@Test
	public void nonVideRemoveNonVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());

		map.remove(4567 + 1);

		Assert.assertNotEquals(0, map.size());
	}

	@Test
	public void nonVideClearVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());

		map.clear();

		Assert.assertEquals(0, map.size());
	}

	@SuppressWarnings("unused")
	@Test
	public void nonVideSizeGetNonVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(null, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());

		Element e = map.get(4567);
		int n = map.size();

		Assert.assertNotEquals(0, map.size());
	}

	@Test
	public void nonVidePutNonVide() {
		Map map = null;
		map = new HashMap(0, 5, 11);

		Assert.assertNotEquals(0, map);
		Assert.assertEquals(0, map.size());

		map.put(4567, new Map.Element(4567, ""));

		Assert.assertNotEquals(0, map.size());

		map.put(4567 + 1, new Map.Element(4567 + 1, ""));

		Assert.assertNotEquals(0, map.size());
	}
}
