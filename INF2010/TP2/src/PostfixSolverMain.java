import java.io.IOException;
import java.util.Stack;

public class PostfixSolverMain {
	public static void main(String[] args) throws IOException {
		Stack<Double> stack = new Stack<Double>();
		// 25+5*2+15/3-5
		String s = "25 5 2 * + 15 3 / 5 - +";

		// L'expression est separee en tokens selon les espaces
		for (String token : s.split("\\s")) {

			try {
				stack.push(Double.valueOf(token));
			} catch (NumberFormatException e) {
				double tmp;
				switch (token) {
				case "+":
					stack.push(stack.pop() + stack.pop());
					break;
				case "*":
					stack.push(stack.pop() * stack.pop());
					break;
				case "/":

					tmp = stack.pop();
					stack.push(stack.pop() / tmp);
					break;

				case "-":
					tmp = stack.pop();
					stack.push(stack.pop() - tmp);
					break;

				}

			}

		}

		System.out.println("25 + 5*2 + 15/3 - 5 = " + stack.peek());
		if (stack.peek() == 35)
			System.out.println("It's all good");
		else
			System.out.println("Erreur: mauvais resultat");
	}
}
