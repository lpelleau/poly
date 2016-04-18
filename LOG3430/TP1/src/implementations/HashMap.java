
package implementations;

import interfaces.List;
import interfaces.Map;

public class HashMap implements Map {
	private int fonctHashage;
	private int maxNodes;
	private int initialCapacity;

	private List tab[];
	private int nbElem = 0;

	public HashMap(int fonctHashage, int maxNodes, int initialCapacity) {
		this.fonctHashage = fonctHashage;
		this.maxNodes = maxNodes;
		this.initialCapacity = initialCapacity;

		clear();
	}

	@Override
	public void put(Integer key, Element e) {
		if (!tab[hashKey(key)].add(e)) {
			rehash();
			put(key, e);
		}
		nbElem++;
	}

	@Override
	public Element remove(Integer key) {
		Element e = tab[hashKey(key)].remove(key);
		if (e != null) {
			nbElem--;
		} 
		return e;
	}

	@Override
	public int size() {
		return nbElem;
	}

	@Override
	public Element get(Integer key) {
		return tab[hashKey(key)].get(key);
	}

	@Override
	public void clear() {
		tab = new LinkedList[this.initialCapacity];
		for (int i = 0; i < this.initialCapacity; i++) {
			tab[i] = new LinkedList(this.maxNodes);
		}
		nbElem = 0;
	}

	@Override
	public void rehash() {
		List[] old = tab;
		tab = new LinkedList[nextPrime(2 * old.length)];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = new LinkedList(this.maxNodes);
		}
		nbElem = 0;

		for (int i = 0; i < old.length; i++) {
			for (int j = 0; j < old[i].size(); j++) {
				Element e = old[i].remove();
				put(e.getId(), e);
			}
		}
	}

	@Override
	public String toString() {
		String res = "";

		for (List l : tab) {
			res += l.toString();
		}

		return res;
	}

	/**
	 * Return the hashed key given in parameter. <br />
	 * Used to select the wanted LinkedLisk.
	 */
	private int hashKey(Integer i) {
		return (fonctHashage == 1) ? i % tab.length
				: Integer.valueOf(i.toString().substring(i.toString().length() - 3, i.toString().length()))
						% tab.length;
	}

	/**
	 * <i>From Ressources.pdf</i> <br />
	 * Retrieve the next prime after the given integer.
	 */
	public static int nextPrime(int n) {
		if (n % 2 == 0)
			n++;
		for (; !isPrime(n); n += 2)
			;
		return n;
	}

	/**
	 * <i>From Ressources.pdf</i> <br />
	 * Return true if n is prime, false else.
	 */
	public static boolean isPrime(int n) {
		if (n == 2 || n == 3)
			return true;
		if (n == 1 || n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2)
			if (n % i == 0)
				return false;
		return true;
	}
}
