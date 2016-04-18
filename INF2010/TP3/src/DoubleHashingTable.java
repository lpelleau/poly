
public class DoubleHashingTable<AnyType> {

	private static final int DEFAULT_TABLE_SIZE = 11;
	private static int R = previousPrime(DEFAULT_TABLE_SIZE);

	/* Construction par défaut de la table de hachage */

	public DoubleHashingTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	/* Constructeur par parametre de la table de hachage */

	public DoubleHashingTable(int size) {
		allocateArray(size);
		makeEmpty();
	}

	/*
	 * Insere un élément dans la table de hachage. Si l'élément est déjà
	 * présent, ne rien faire
	 */

	public void insert(AnyType x) {
		// On insert x marqué actif
		int currentPos = findPos(x);
		if (isActive(currentPos))
			return;

		array[currentPos] = new HashEntry<AnyType>(x, true);

		// Si nécéssaire, on agrandit la table

		if (++currentSize > array.length / 2)
			rehash();
	}

	/* Méthode qui agrandit la table */

	private void rehash() {
		HashEntry<AnyType>[] oldArray = array;

		// Création d'une table deux fois plus grande et vide
		allocateArray(nextPrime(2 * oldArray.length));
		currentSize = 0;

		// Copie des éléments de l'ancienne table dans la nouvelle
		for (int i = 0; i < oldArray.length; i++) {
			if (oldArray[i] != null && oldArray[i].isActive)
				insert(oldArray[i].element);
		}

		// N a changé, on choisit donc un nouveau R
		R = previousPrime(array.length);

	}

	/*
	 * Méthode qui trouve une place dans la table de hachage pour un nouvel
	 * élément x
	 */

	private int findPos(AnyType x) {

		int currentPos = myhash(x);
		int i = 0;

		// Tant qu'il y a collision, on cherche une nouvelle position par la
		// double fonction de dispersement, i s'incrémente de 1 à chaque
		// itération
		while (array[currentPos] != null && !array[currentPos].element.equals(x)) {
			i++;
			currentPos = (myhash(x) + i * myhash2(x)) % array.length;
		}

		return currentPos;
	}

	/* Première fonction de dispersement */
	private int myhash(AnyType x) {
		int hashVal = x.hashCode();

		hashVal %= array.length;
		if (hashVal < 0)
			hashVal += array.length;

		return hashVal;

	}

	/* Deuxième fonction de dispersement */
	private int myhash2(AnyType x) {
		int hashVal = x.hashCode();

		hashVal = R - (hashVal % R);

		if (hashVal < 0)
			hashVal += R;

		return hashVal;
	}

	/* Retire un élément de la table de hachage */
	public void remove(AnyType x) {
		int currentPos = findPos(x);
		if (isActive(currentPos))
			array[currentPos].isActive = false;
	}

	/* Retourne si un élément est contenu à cette position */
	public boolean contains(AnyType x) {
		int currentPos = findPos(x);
		return isActive(currentPos);
	}

	/* Vérifie si currentPos existe et est active */
	private boolean isActive(int currentPos) {
		return array[currentPos] != null && array[currentPos].isActive;
	}

	/* Vide la table */
	public void makeEmpty() {
		currentSize = 0;
		for (int i = 0; i < array.length; i++)
			array[i] = null;
	}

	private static class HashEntry<AnyType> {
		public AnyType element; // the element
		public boolean isActive; // false if marked deleted

		public HashEntry(AnyType e) {
			this(e, true);
		}

		public HashEntry(AnyType e, boolean i) {
			element = e;
			isActive = i;
		}
	}

	private HashEntry<AnyType>[] array; // Notre table
	private int currentSize; // Le nombre de cases occupées

	/* Méthode appelée par le constructeur pour créer la table */
	private void allocateArray(int arraySize) {
		array = new HashEntry[nextPrime(arraySize)];
	}

	/* Méthode trouvant le prochain nombre premier après le parametre n */
	private static int nextPrime(int n) {
		if (n % 2 == 0)
			n++;

		for (; !isPrime(n); n += 2)
			;

		return n;
	}

	/* Teste si le parametre n est un nombre premier */
	private static boolean isPrime(int n) {
		if (n == 2 || n == 3)
			return true;

		if (n == 1 || n % 2 == 0)
			return false;

		for (int i = 3; i * i <= n; i += 2)
			if (n % i == 0)
				return false;

		return true;
	}

	/* Renvoie le nombre premier précédent le parametre n */
	private static int previousPrime(int n) {
		if (n % 2 == 0)
			n++;

		for (; !isPrime(n); n -= 2)
			;

		return n;
	}

	/* Renvoie le nombre d'éléments dans la table */
	public int nbElement() {
		return currentSize;
	}
	/*Renvoie l'élément associé à la position*/
	public AnyType get(AnyType x) {
		int currentPos = findPos(x);
		if (isActive(currentPos))
			return array[currentPos].element;
		else
			return null;
	}
}
