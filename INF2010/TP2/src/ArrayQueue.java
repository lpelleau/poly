
public class ArrayQueue<AnyType> implements Queue<AnyType> {
	private static final int DEFAULT_CAPACITY = 10;
	private int size = 0; // Nombre d'elements dans la file.
	private int startindex = 0; // Index du premier element de la file
	private AnyType[] table;

	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		this.table = (AnyType[]) new Object[DEFAULT_CAPACITY];
	}

	// Indique si la file est vide
	public boolean empty() {
		return size == 0;
	}

	// Retourne la taille de la file
	public int size() {
		return size;
	}

	// Retourne l'element en tete de file
	// Retourne null si la file est vide
	// complexité asymptotique: O(1)
	public AnyType peek() {
		return table[startindex];
	}

	// Retire l'element en tete de file
	// complexité asymptotique: O(1)
	public void pop() throws EmptyQueueException {
		if (empty()) {
			throw new EmptyQueueException();
		}
		startindex++;
		size--;
	}

	// Ajoute un element a la fin de la file
	// Double la taille de la file si necessaire (utiliser la fonction resize
	// definie plus bas)
	// complexité asymptotique: O(1) ( O(N) lorsqu'un redimensionnement est
	// necessaire )
	public void push(AnyType item) {
		if (startindex + size == table.length) {
			resize(2);
		}
		table[startindex + size] = item;
		size++;
	}

	// Redimensionne la file. La capacite est multipliee par un facteur de
	// resizeFactor.
	// Replace les elements de la file au debut du tableau
	// complexité asymptotique: O(N)
	@SuppressWarnings("unchecked")
	private void resize(int resizeFactor) {
		AnyType[] tmp = (AnyType[]) new Object[table.length * resizeFactor];

		for (int i = startindex; i < startindex + size; i++) {
			tmp[i - startindex] = table[i];
		}
		table = tmp;
		startindex = 0;
	}

	public static void main(String argv[]) {
		try {
			Queue<Integer> test = new ArrayQueue<Integer>();
			test.push(4);
			test.push(5);
			test.push(8);
			test.push(7);
			test.pop();
			test.pop();
			test.pop();
			test.pop();
			try {
				test.pop();
			} catch (EmptyQueueException e) {
				System.out.println("pop : ok");
			}
			test.push(4);
			test.push(5);
			test.push(8);
			test.push(7);
			test.push(4);
			test.push(5);
			test.push(8);
			test.push(7);
			test.push(4);
			test.push(5);
			test.push(8);
			test.push(7);
			while (test.peek() != null) {
				System.out.print(test.peek() + " ");
				test.pop();
			}
		} catch (EmptyQueueException e) {
			e.printStackTrace();
		}
	}
}
