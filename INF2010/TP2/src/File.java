
@SuppressWarnings("unchecked")
public class File<AnyType> {
	private static final int INITIAL_SIZE = 10;

	private AnyType[] tab = (AnyType[]) new Object[INITIAL_SIZE];
	private int dessus = 0, dessous = 0;

	public void add(AnyType o) {
		tab[dessus] = o;
		++dessus;

		if (dessus == tab.length) {
			resize(tab.length * 2);
		}
	}

	public AnyType remove() {
		if (dessus - dessous == 0) {
			return null;
		}

		return tab[dessous++];
	}

	private void resize(int size) {
		if (size < dessus - dessous) {
			return;
		}

		AnyType[] tmp = (AnyType[]) new Object[size];

		for (int i = 0; i < dessus - dessous; i++) {
			tmp[i] = tab[dessous + i];
		}

		tab = tmp;
		dessus = dessus - dessous;
		dessous = 0;

	}

	public void optimizeMemory() {
		resize(dessus - dessous);
	}

	public static void main(String argv[]) {
		File<Integer> test = new File<Integer>();
		test.add(4);
		test.add(5);
		test.add(8);
		test.add(7);
		test.remove();
		test.remove();
		test.remove();
		test.remove();
		test.remove();
		test.add(4);
		test.add(5);
		test.add(8);
		test.add(7);
		test.add(4);
		test.add(5);
		test.add(8);
		test.add(7);
		test.add(4);
		test.add(5);
		test.add(8);
		test.add(7);
		test.optimizeMemory();
		Integer i = test.remove();
		while (i != null) {
			System.out.println(i);
			i = test.remove();
		}

	};
}
