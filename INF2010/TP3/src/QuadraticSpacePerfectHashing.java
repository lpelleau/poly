import java.util.ArrayList;
import java.util.Random;

public class QuadraticSpacePerfectHashing<AnyType> {
	static int p = 46337;

	int a, b;
	AnyType[] items;

	QuadraticSpacePerfectHashing() {
		a = b = 0;
		items = null;
	}

	QuadraticSpacePerfectHashing(ArrayList<AnyType> array) {
		AllocateMemory(array);
	}

	public void SetArray(ArrayList<AnyType> array) {
		AllocateMemory(array);
	}

	public int Size() {
		if (items == null)
			return 0;

		return items.length;
	}

	public boolean contains(AnyType x) {
		if (Size() == 0)
			return false;

		if (a == 0)
			return (items[0].equals(x));
		int m = items.length;
		int index = ((a * x.hashCode() + b) % p) % m;

		index = (index < 0 ? index + m : index);

		return ((items[index] != null) && (items[index].equals(x)));
	}

	@SuppressWarnings("unchecked")
	private void AllocateMemory(ArrayList<AnyType> array) {
		Random generator = new Random(System.nanoTime());

		if (array == null || array.size() == 0) {
			// ACOMPLETE
			a = b = 0;
			items = null;
			return;
		}
		if (array.size() == 1) {
			a = b = 0;
			// ACONPETER
			items = (AnyType[]) new Object[1];
			items[0] = array.get(0);
			return;
		}

		do {
			a = generator.nextInt(p - 1) + 1;
			b = generator.nextInt(p);
			items = (AnyType[]) new Object[array.size() * array.size()];

			// A completer
			for (AnyType o : array) {
				items[getIndex(o)] = o;
			}
		} while (collisionExists(array));
	}

	@SuppressWarnings("unchecked")
	private boolean collisionExists(ArrayList<AnyType> array) {
		// A completer
		for (AnyType o : array) {
			AnyType o2 = get(o);
			if (o2 != null && !o2.equals(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * On aurait pu modifier la méthode contains afin d'éviter la duplication de
	 * code.<br/>
	 * Ca n'a pas été fait afin de ne pas modifier le code fourni.
	 */
	private int getIndex(AnyType x) {
		int m = items.length;
		int index = ((a * x.hashCode() + b) % p) % m;
		return (index < 0 ? index + m : index);
	}

	/**
	 * On aurait pu modifier la méthode contains afin d'éviter la duplication de
	 * code.<br/>
	 * Ca n'a pas été fait afin de ne pas modifier le code fourni.
	 */
	private AnyType get(AnyType x) {
		if (Size() == 0)
			return null;

		if (a == 0)
			return items[0];

		return items[getIndex(x)];
	}
}
