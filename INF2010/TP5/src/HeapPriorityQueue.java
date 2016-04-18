import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;

public class HeapPriorityQueue<AnyType> implements PriorityQueue<AnyType> {

	private static final int DEFAULT_CAPACITY = 20;

	private int currentSize; // nombre d'�l�ments pr�sents
	private PQEntry<AnyType>[] items; // tableau contenant les �l�ments ordonn�s
										// en tas
	Hashtable<AnyType, Integer> indexMap; // Mappe associant � chaque �l�ment
											// Anytype un entier
											// (sa position dans items)

	/**
	 * M�thodes d�j� impl�ment�es
	 */
	public HeapPriorityQueue() {
		initialize();
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		initialize();
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean contains(AnyType x) {
		return indexMap.containsKey(x);
	}

	@SuppressWarnings("unchecked")
	private void enlargeArray(int newSize) {
		if (newSize <= (currentSize + 1))
			return;

		PQEntry<AnyType>[] old = items;
		items = (PQEntry<AnyType>[]) new PQEntry[newSize];

		for (int i = 0; i <= old.length; i++)
			items[i] = old[i];
	}

	private void initialize() {
		initialize(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	private void initialize(int capacity) {
		if (capacity < 0)
			capacity = DEFAULT_CAPACITY;
		currentSize = 0;
		items = (PQEntry<AnyType>[]) new PQEntry[capacity];
		indexMap = new Hashtable<AnyType, Integer>(capacity);
	}

	public String toString() {
		String output = "";

		for (int i = 1; i <= currentSize; i++)
			output += items[i] + ((i != currentSize) ? ", " : "");

		return output;
	}

	/**
	 * M�thodes � impl�menter
	 */

	// Commentez la ligne de add1 et d�commenter la ligne de add2 pour
	// l'exercice 5
	public boolean add(AnyType x, int priority) throws NullPointerException, IllegalArgumentException {
		// return add1(x, priority); // Exercice 1
		return add2(x, priority); // Exercice 5
	}

	/**
	 * Exercice 1
	 */
	private boolean add1(AnyType x, int priority) throws NullPointerException, IllegalArgumentException {
		// completer
		if (priority < 0)
			throw new IllegalArgumentException();
		if (x == null)
			throw new NullPointerException();

		PQEntry<AnyType> n = new PQEntry<>(x, priority);

		if (currentSize == items.length - 1)
			enlargeArray(items.length * 2 + 1);

		int hole = ++currentSize;

		for (; hole > 1 && priority < items[hole / 2].priority; hole /= 2)
			items[hole] = items[hole / 2];

		items[hole] = n;

		return true;
	}

	// Commentez la ligne de poll1 et d�commenter la ligne de poll2 pour
	// l'exercice 6
	public AnyType poll() {
		// return poll1(); // Exercice 2
		return poll2(); // Exercice 6
	}

	/**
	 * Exercice 2
	 */
	private AnyType poll1() {
		// completer
		if (isEmpty())
			throw new EmptyStackException();

		PQEntry<AnyType> maxPriority = items[1];
		items[1] = items[currentSize--];
		percolateDown1(1);

		return maxPriority.value;
	}

	private void percolateDown1(int hole) {
		// completer
		int child;
		PQEntry<AnyType> tmp = items[hole];

		for (; hole * 2 <= currentSize; hole = child) {
			child = hole * 2;

			if (child != currentSize && items[child + 1].priority < items[child].priority)
				child++;

			if (items[child].priority < tmp.priority)
				items[hole] = items[child];
			else
				break;
		}

		items[hole] = tmp;
	}

	// Commentez la ligne de buildHeap1 et d�commenter la ligne de buildHeap2
	// pour l'exercice 7
	public HeapPriorityQueue(AnyType[] items, int[] priorities) throws IllegalArgumentException, NullPointerException {
		// buildHeap1(items, priorities); // Exercice 3
		buildHeap2(items, priorities); // Exercice 7
	}

	/**
	 * Exercice 3
	 */
	private void buildHeap1(AnyType[] items, int[] priorities) throws IllegalArgumentException, NullPointerException {
		// completer
		if (items == null || priorities == null)
			throw new NullPointerException();

		for (AnyType o : items)
			if (o == null)
				throw new NullPointerException();

		for (int p : priorities)
			if (p < 0)
				throw new IllegalArgumentException();

		initialize();

		for (int i = 0; i < items.length && i < priorities.length; i++)
			add1(items[i], priorities[i]);
	}

	/**
	 * Exercice 4
	 */
	public ArrayList<AnyType> getMax() {
		// completer
		if (isEmpty())
			return new ArrayList<>();

		ArrayList<AnyType> res = new ArrayList<>();
		int maxP = -1;
		for (int i = 1; i <= currentSize; i++) {
			if (items[i].priority > maxP) {
				maxP = items[i].priority;
				res.clear();
			}
			if (items[i].priority == maxP) {
				res.add(items[i].value);
			}
		}
		return res;
	}

	/**
	 * Exercice 5
	 */
	private boolean add2(AnyType x, int priority) throws IllegalArgumentException {
		// Completer
		if (priority < 0)
			throw new IllegalArgumentException();
		if (x == null)
			throw new NullPointerException();

		if (indexMap.contains(x))
			return true;

		PQEntry<AnyType> n = new PQEntry<>(x, priority);

		if (currentSize == items.length - 1)
			enlargeArray(items.length * 2 + 1);

		int hole = ++currentSize;

		for (; hole > 1 && priority < items[hole / 2].priority; hole /= 2)
			items[hole] = items[hole / 2];

		items[hole] = n;
		indexMap.put(x, hole);
		

		return true;
	}

	/**
	 * Exercice 6
	 */
	private AnyType poll2() {
		// completer
		if (isEmpty())
			throw new EmptyStackException();

		PQEntry<AnyType> maxPriority = items[1];
		items[1] = items[currentSize--];
		percolateDown2(1);
		indexMap.remove(maxPriority.value);

		return maxPriority.value;
	}

	private void percolateDown2(int hole) {
		// completer
		int child;
		PQEntry<AnyType> tmp = items[hole];

		for (; hole * 2 <= currentSize; hole = child) {
			child = hole * 2;

			if (child != currentSize && items[child + 1].priority < items[child].priority)
				child++;

			if (items[child].priority < tmp.priority) {
				indexMap.replace(items[child].value, hole);
				items[hole] = items[child];
			} else
				break;
		}

		indexMap.replace(tmp.value, hole);
		items[hole] = tmp;
	}

	/**
	 * Exercice 7
	 */
	private void buildHeap2(AnyType[] items, int[] priorities) throws IllegalArgumentException, NullPointerException {
		// completer
		if (items == null || priorities == null)
			throw new NullPointerException();

		for (AnyType o : items)
			if (o == null)
				throw new NullPointerException();

		for (int p : priorities)
			if (p < 0)
				throw new IllegalArgumentException();

		initialize();

		for (int i = 0; i < items.length && i < priorities.length; i++)
			add2(items[i], priorities[i]);
	}

	/**
	 * Exercice 8
	 */
	public void updatePriority(AnyType x, int priority) {
		// completer
		if(x==null)
			throw new NullPointerException();
		if(priority < 0)
			throw new IllegalArgumentException();

		Integer index = indexMap.get(x);
	
		if (index == null)
			return;

		while(index != 1){
			PQEntry<AnyType> tmp = items[index];
			items[index]=items[index/2];
			items[index/2] = tmp;
			index /= 2;			
		}
		poll2();
		add2(x, priority);
	}
}
