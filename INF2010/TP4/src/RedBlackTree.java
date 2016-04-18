import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T>> {
	private RBNode<T> root; // Racine de l'arbre
	private String display;

	enum ChildType {
		left, right
	}

	public RedBlackTree() {
		root = null;
	}

	public void printFancyTree() {
		printFancyTree(root, "", ChildType.right);
	}

	private void printFancyTree(RBNode<T> t, String prefix, ChildType myChildType) {
		System.out.print(prefix + "|__"); // un | et trois _

		if (t != null) {
			boolean isLeaf = (t.isNil()) || (t.leftChild.isNil() && t.rightChild.isNil());

			System.out.println(t);
			String _prefix = prefix;

			if (myChildType == ChildType.left)
				_prefix += "|   "; // un | et trois espaces
			else
				_prefix += "   "; // trois espaces

			if (!isLeaf) {
				printFancyTree(t.leftChild, _prefix, ChildType.left);
				printFancyTree(t.rightChild, _prefix, ChildType.right);
			}
		} else
			System.out.print("null\n");
	}

	public T find(int key) {
		return find(root, key);
	}

	private T find(RBNode<T> current, int key) {
		// À COMPLÉTER

		if (current != null) {
			int hash = current.hashCode();
			if (key == hash) {
				return current.value;
			} else if (key < hash) {
				return find(current.leftChild, key);
			} else {
				return find(current.rightChild, key);
			}
		} else {
			return null;
		}
	}

	public void insert(T val) {
		insertNode(new RBNode<T>(val));
	}

	private void insertNode(RBNode<T> newNode) {
		if (root == null) // Si arbre vide
			root = newNode;
		else {
			RBNode<T> position = root; // On se place a la racine

			while (true) // on insere le noeud (ABR standard)
			{
				int newKey = newNode.value.hashCode();
				int posKey = position.value.hashCode();

				if (newKey < posKey) {
					if (position.leftChild.isNil()) {
						position.leftChild = newNode;
						newNode.parent = position;
						break;
					} else
						position = position.leftChild;
				} else if (newKey > posKey) {
					if (position.rightChild.isNil()) {
						position.rightChild = newNode;
						newNode.parent = position;
						break;
					} else
						position = position.rightChild;
				} else // pas de doublons
					return;
			}
		}

		insertionCases(newNode);
	}

	private void insertionCases(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		insertionCase1(X);
	}

	private void insertionCase1(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		if (root == X) {
			root.setToBlack();
		} else {
			insertionCase2(X);
		}
	}

	private void insertionCase2(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		if (X.parent.isRed()) {
			insertionCase3(X);
		}
	}

	private void insertionCase3(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		if (X.parent.isRed() && X.uncle().isRed()) {
			X.parent.setToBlack();
			X.uncle().setToBlack();
			X.grandParent().setToRed();
			insertionCase1(X.grandParent());
		} else {
			insertionCase4(X);
		}
	}

	private void insertionCase4(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		if (X.parent.isRed() && X.uncle().isBlack()) {
			if (X.parent.leftChild == X && X.grandParent().rightChild == X.parent) {
				rotateRight(X.parent);
				insertionCase5(X.rightChild);
			} else if (X.parent.rightChild == X && X.grandParent().leftChild == X.parent) {
				rotateLeft(X.parent);
				insertionCase5(X.leftChild);
			} else {
				insertionCase5(X);
			}
		} else {
			insertionCase5(X);
		}
	}

	private void insertionCase5(RBNode<T> X) {
		// A MODIFIER/COMPLÉTER

		if (X.parent.isRed() && X.uncle().isBlack()) {
			if (X.parent.rightChild == X && X.grandParent().rightChild == X.parent) {
				if (X.grandParent().isBlack()) {
					X.grandParent().setToRed();
				} else {
					X.grandParent().setToBlack();
				}
				if (X.parent.isBlack()) {
					X.parent.setToRed();
				} else {
					X.parent.setToBlack();
				}
				rotateLeft(X.grandParent());
			} else if (X.parent.leftChild == X && X.grandParent().leftChild == X.parent) {
				if (X.grandParent().isBlack()) {
					X.grandParent().setToRed();
				} else {
					X.grandParent().setToBlack();
				}
				if (X.parent.isBlack()) {
					X.parent.setToRed();
				} else {
					X.parent.setToBlack();
				}
				rotateRight(X.grandParent());
			}
		}
	}

	private void rotateLeft(RBNode<T> G) {
		// A MODIFIER/COMPLÉTER

		RBNode<T> rightChild = G.rightChild;
		G.rightChild = new RBNode<>();
		G.parent.leftChild = rightChild;
		rightChild.leftChild = G;
		rightChild.parent = G.parent;
		G.parent = rightChild;
	}

	private void rotateRight(RBNode<T> G) {
		// A MODIFIER/COMPLÉTER

		RBNode<T> leftChild = G.leftChild;
		G.leftChild = new RBNode<>();
		G.parent.rightChild = leftChild;
		leftChild.rightChild = G;
		leftChild.parent = G.parent;
		G.parent = leftChild;
	}

	public void printTreePreOrder() {
		if (root == null)
			System.out.println("Empty tree");
		else {
			System.out.print("PreOrdre ( ");
			display = "";
			printTreePreOrder(root);
			System.out.print(display.substring(0, display.length() - 2));
			System.out.println(" )");
		}
		return;
	}

	private void printTreePreOrder(RBNode<T> P) {
		// A MODIFIER/COMPLÉTER

		if (!P.isNil()) {
			display += "{" + P.toString() + "}, ";
			// System.out.print("{" + P.toString() + "}, ");
			printTreePreOrder(P.leftChild);
			printTreePreOrder(P.rightChild);
		}
	}

	public void printTreePostOrder() {
		if (root == null)
			System.out.println("Empty tree");
		else {
			System.out.print("PostOrdre ( ");
			display = "";
			printTreePostOrder(root);
			System.out.print(display.substring(0, display.length() - 2));
			System.out.println(" )");
		}
		return;
	}

	private void printTreePostOrder(RBNode<T> P) {
		// A MODIFIER/COMPLÉTER

		if (!P.isNil()) {
			printTreePostOrder(P.leftChild);
			printTreePostOrder(P.rightChild);
			display += "{" + P.toString() + "}, ";
			// System.out.print("{" + P.toString() + "}, ");
		}
	}

	public void printTreeAscendingOrder() {
		if (root == null)
			System.out.println("Empty tree");
		else {
			System.out.print("AscendingOrdre ( ");
			display = "";
			printTreeAscendingOrder(root);
			System.out.print(display.substring(0, display.length()));
			System.out.println(" )");
		}
		return;
	}

	private void printTreeAscendingOrder(RBNode<T> P) {
		// A COMPLÉTER

		if (!P.isNil()) {
			printTreeAscendingOrder(P.leftChild);
			display += "{" + P.toString() + "}, ";
			// System.out.print("{" + P.toString() + "}, ");
			printTreeAscendingOrder(P.rightChild);
		}
	}

	public void printTreeDescendingOrder() {
		if (root == null)
			System.out.println("Empty tree");
		else {
			System.out.print("DescendingOrdre ( ");
			display = "";
			printTreeDescendingOrder(root);
			System.out.print(display.substring(0, display.length()));
			System.out.println(" )");
		}
		return;
	}

	private void printTreeDescendingOrder(RBNode<T> P) {
		// A COMPLÉTER

		if (!P.isNil()) {
			printTreeDescendingOrder(P.rightChild);
			display += "{" + P.toString() + "}, ";
			// System.out.print("{" + P.toString() + "}, ");
			printTreeDescendingOrder(P.leftChild);
		}
	}

	public void printTreeLevelOrder() {
		if (root == null)
			System.out.println("Empty tree");
		else {
			System.out.print("LevelOrdre ( ");

			Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();

			q.add(root);

			// À COMPLÉTER
			RBNode<T> N = q.poll();
			display = "";
			while (!N.isNil()) {
				display += "{" + N + "}, ";
				q.add(N.leftChild);
				q.add(N.rightChild);
				N = q.poll();
			}
			System.out.print(display.substring(0, display.length() - 2));

			System.out.println(" )");
		}
	}

	private static class RBNode<T extends Comparable<? super T>> {
		enum RB_COLOR {
			BLACK, RED
		} // Couleur possible

		RBNode<T> parent; // Noeud parent
		RBNode<T> leftChild; // Feuille gauche
		RBNode<T> rightChild; // Feuille droite
		RB_COLOR color; // Couleur du noeud
		T value; // Valeur du noeud

		// Constructeur (NIL)
		RBNode() {
			setToBlack();
		}

		// Constructeur (feuille)
		RBNode(T val) {
			setToRed();
			value = val;
			leftChild = new RBNode<T>();
			leftChild.parent = this;
			rightChild = new RBNode<T>();
			rightChild.parent = this;
		}

		RBNode<T> grandParent() {
			// À COMPLÉTER

			RBNode<T> t = this.parent;
			if (t != null) {
				t = t.parent;

				if (t == null)

					return null;

				else
					return t;
			} else
				return null;
		}

		RBNode<T> uncle() {
			// À COMPLÉTER

			RBNode<T> t = this.parent;
			if (t != null) {
				t = t.parent;
				if (t == null)
					return null;
				else
					return this.parent == t.leftChild ? t.rightChild : t.leftChild;
			} else
				return null;
		}

		RBNode<T> sibling() {
			// À COMPLÉTER

			RBNode<T> t = this.parent;
			if (t == null)
				return null;
			else
				return this == t.leftChild ? t.rightChild : t.leftChild;

		}

		public String toString() {
			return value + " (" + (color == RB_COLOR.BLACK ? "black" : "red") + ")";
		}

		boolean isBlack() {
			return (color == RB_COLOR.BLACK);
		}

		boolean isRed() {
			return (color == RB_COLOR.RED);
		}

		boolean isNil() {
			return (leftChild == null) && (rightChild == null);
		}

		void setToBlack() {
			color = RB_COLOR.BLACK;
		}

		void setToRed() {
			color = RB_COLOR.RED;
		}

		@Override
		public int hashCode() {
			return value == null ? -1 : value.hashCode();
		}
	}
}
