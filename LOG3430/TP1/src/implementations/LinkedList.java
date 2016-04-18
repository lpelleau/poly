package implementations;

import interfaces.List;
import interfaces.Map.Element;

/**
 * Represent a Linked list based on the interface of List. <br />
 * The class have only access to the first element, each element element have
 * access to it's follower. <br />
 * A LinkedList have a maximum number of elements given by the parameter maxNode
 * to the constructor.
 */
public class LinkedList implements List {
	private int maxNodes;
	private Node first = null;
	private int nbElem = 0;

	public LinkedList(int maxNodes) {
		this.maxNodes = maxNodes;
	}

	@Override
	public boolean add(Element e) {
		if (get(e.getId()) != null) {
			remove(e.getId());
		}
		if (nbElem < maxNodes) {
			Node n = new Node(e);
			if (nbElem == 0) {
				first = n;
			} else {
				Node last = first;
				while (last.next != null) {
					last = last.next;
				}
				last.next = n;
			}
			nbElem++;
			return true;
		}
		return false;
	}

	@Override
	public Element remove(Integer key) {
		if (nbElem != 0) {
			Node current = first;
			if (current.e.getId().equals(key)) {
				first = current.next;
				nbElem--;
				return current.e;
			}
			Node prec = null;
			while (current.next != null) {
				prec = current;
				current = current.next;
				if (current.e.getId().equals(key)) {
					prec.next = current.next;
					nbElem--;
					return current.e;
				}
			}
		}

		return null;
	}

	@Override
	public Element remove() {
		if (nbElem != 0) {
			Node n = first;
			first = n.next;
			nbElem--;
			return n.e;
		}

		return null;
	}

	@Override
	public Element get(Integer key) {
		if (nbElem != 0) {
			Node current = first;
			if (current.e.getId().equals(key)) {
				return current.e;
			}
			while (current.next != null) {
				current = current.next;
				if (current.e.getId().equals(key)) {
					return current.e;
				}
			}
		}

		return null;
	}

	@Override
	public int size() {
		return nbElem;
	}

	@Override
	public String toString() {
		String res = "";

		if (nbElem != 0) {
			Node current = first;
			res += current.toString();
			while (current.next != null) {
				current = current.next;
				res += current.toString();
			}
		}

		return res + "\n";
	}

	/**
	 * Private class to allow the link between the elements. <br />
	 * Have two attributes : the element and the <i>Node</i> follower.
	 */
	private static class Node {
		private Element e;
		private Node next = null;

		public Node(Element e) {
			this.e = e;
		}

		@Override
		public String toString() {
			return e.toString();
		}
	}
}
