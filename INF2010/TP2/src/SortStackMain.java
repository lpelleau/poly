import java.util.Random;
import java.util.Stack;

public class SortStackMain {
	static final int COUNT = 30;
	static final int MAX_VALUE = 1000;

	public static void main(String[] args) {
		boolean sortIsGood = true;

		Random generator = new Random(System.nanoTime());
		Stack<Integer> stack = new Stack<Integer>();

		for (int i = 0; i < COUNT; i++)
			stack.push(generator.nextInt(MAX_VALUE));

		stack = sortStack(stack);

		boolean countIsGood = size(stack) == COUNT;

		int tmp = stack.pop();
		while (!stack.isEmpty()) {
			System.out.print(tmp + ", ");

			if (tmp > stack.peek())
				sortIsGood = false;

			tmp = stack.pop();
		}
		System.out.println(tmp);

		if (!countIsGood)
			System.out.println("Erreur: il manque des elements dans la pile");
		else if (!sortIsGood)
			System.out.println("Erreur: le trie a echoue");
		else
			System.out.println("It's all good");
	}

	private static int size(Stack<Integer> stack) {
		int count = 0;
		Stack<Integer> tmp = new Stack<Integer>();

		while (!stack.empty()) {
			tmp.push(stack.pop());
			count++;
		}

		for (int i = 0; i < count; i++) {
			stack.push(tmp.pop());
		}
		return count;

	}

	static Stack<Integer> sortStack(Stack<Integer> stack) {
		Stack<Integer> tmp = new Stack<Integer>();

		// La pile à traiter est vide : déjà tirée
		if (stack.empty()) {
			return stack;
		}

		// Vérification que la pile ne soit pas déjà triée
		boolean sortIsGood = true;
		tmp.push(stack.pop());
		do {
			if (tmp.peek() > stack.peek()) {
				sortIsGood = false;
			}
			tmp.push(stack.pop());
		} while (!stack.isEmpty());

		// Si elle est triée, on la remet dans le bon sens, puis on quitte la
		// méthode
		if (sortIsGood) {
			stack.push(tmp.pop());
			do {
				stack.push(tmp.pop());
			} while (!tmp.isEmpty());
			return stack;
		}

		// Tri de la pile non triée
		stack.push(tmp.pop());
		while (!tmp.empty()) {
			// Éléments au dessus de chaque pile
			Integer tmpPivot = tmp.peek();
			Integer topStack = stack.pop();

			// Lorsque l'ordre n'est pas respecté...
			boolean swap = false;
			while (tmpPivot > topStack) {
				swap = true;
				// On place l'élément en tête de pile sur l'autre pile...
				tmp.push(topStack);
				// Jusqu'à ce qu'on ai trouvé sa place
				if (stack.empty() || tmpPivot <= stack.peek()) {
					break;
				}
				topStack = stack.pop();
			}

			// Si on a déplacé des éléments entre les piles...
			if (swap) {
				// On place le pivot à l'endroit voulu
				stack.push(tmpPivot);
				// On les remet à leur place, jusqu'à rencontrer l'élément pivot
				stack.push(tmp.pop());
				while (tmpPivot != tmp.peek()) {
					stack.push(tmp.pop());
				}
				tmp.pop();

			} else {
				// On place les éléments dans l'ordre dans la pile
				stack.push(topStack);
				stack.push(tmp.pop());
			}
		}
		return stack;
	}
}
