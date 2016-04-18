
public class LinkedListQueue<AnyType> implements Queue<AnyType> {
	// Un noeud de la file
	@SuppressWarnings("hiding")
	private class Node<AnyType> {
		private AnyType data;
		private Node next;

		public Node(AnyType data, Node next) {
			this.data = data;
			this.next = next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public Node<AnyType> getNext() {
			return next;
		}

		public AnyType getData() {
			return data;
		}
	}

	private int size = 0; // Nombre d'elements dans la file.
	private Node<AnyType> first;// Premier element de la liste
	private Node<AnyType> last; // Dernier element de la liste

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
		if (first != null) {
			return first.getData();
		} else {
			return null;
		}
	}

	// Retire l'element en tete de file
	// complexité asymptotique: O(1)
	public void pop() throws EmptyQueueException {
		if (first != null) {
			first = first.getNext();
			size--;
		} else {
			throw new EmptyQueueException();
		}

	}

	// Ajoute un element a la fin de la file
	// complexité asymptotique: O(1)
	public void push(AnyType item) {

		Node<AnyType> newNode = new Node<AnyType>(item, null);

		if (!empty()) {
			last.setNext(newNode);
		} else {
			first = newNode;
		}
		last = newNode;
		size++;

	}

	public static void main(String argv[]) {
		try {
			Queue<Integer> test = new LinkedListQueue<Integer>();
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
