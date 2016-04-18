import java.util.ArrayList;

public class TestPriorityQueue {
	public static void main(String[] args) {
		final boolean testPartII = true; // Changer pour true pour la partie II

		// �l�ments
        String[] items = {
                "v_01", "v_02", "v_03", "v_04", "v_05", 
                "v_06", "v_07", "v_08", "v_09", "v_10", 
                "v_11", "v_12", "v_13", "v_14", "v_15"};

		// Priorit�s
		int[] priorities = { 4, 5, 6, 1, 2, 5, 3, 4, 2, 6, 3, 4, 6, 2, 5 };

		// File de priorit�
		HeapPriorityQueue<String> hpq;

		// Instantiation
		hpq = new HeapPriorityQueue<String>();

		// Teste exercice 1/5
		for (int i = 0; i < priorities.length; i++) {
			System.out.println("Insert " + items[i] + " avec priorit� " + priorities[i]);
			hpq.add(items[i], priorities[i]);
		}

		System.out.println("R�sultat:\n" + hpq.toString() + "\n");

		// Teste exercice 2/6
		while (!hpq.isEmpty()) {
			System.out.println("Retire " + hpq.poll() + ", r�sultat:");
			System.out.println(hpq.toString());
		}

		// Teste exercice 3/7
		System.out.println("Constructeur avec param�tres:");
		// items[9] = items[0];
		System.out.println(hpq = new HeapPriorityQueue<String>(items, priorities));

		// Teste exercice 4
		System.out.println("Valeurs max:");
		ArrayList<String> al = hpq.getMax();

		for (String item : al)
			System.out.print(item + ", ");

		System.out.println();

		if (!testPartII)
			return;

		// Teste exercice 8
		System.out.println("\nModifie priorit� de v_13 avec priorit� 0");
		hpq.updatePriority("v_13", 0);
		System.out.println(hpq.toString());
	}
}
