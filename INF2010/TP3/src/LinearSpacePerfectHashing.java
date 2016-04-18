import java.util.ArrayList;
import java.util.Random;

public class LinearSpacePerfectHashing<AnyType> {
	static int p = 46337;

	QuadraticSpacePerfectHashing<AnyType>[] data;
	int a, b;

	LinearSpacePerfectHashing() {
		a = b = 0;
		data = null;
	}

	LinearSpacePerfectHashing(ArrayList<AnyType> array) {
		AllocateMemory(array);
	}

	public void SetArray(ArrayList<AnyType> array) {
		AllocateMemory(array);
	}

	@SuppressWarnings("unchecked")
	private void AllocateMemory(ArrayList<AnyType> array) {
		Random generator = new Random(System.nanoTime());

		if (array == null || array.size() == 0) {
			// A completer
			a = b = 0;
			data = null;
			return;
		}
		if (array.size() == 1) {
			a = b = 0;

			// A completer
			data = new QuadraticSpacePerfectHashing[1];
			data[0] = new QuadraticSpacePerfectHashing<>(array);
			return;
		}

		// A completer
		a = generator.nextInt(p - 1) + 1;
		b = generator.nextInt(p);
		data = new QuadraticSpacePerfectHashing[array.size()];
		ArrayList<AnyType>[] tmp = new ArrayList[array.size()]; 
		for (AnyType o : array) {
			int index = getIndex(o);
			if (tmp[index] == null) {
				tmp[index] = new ArrayList<>();
			}
			tmp[index].add(o);
		}
		for (int i = 0 ; i < tmp.length ; i++) {
			if (tmp[i] != null) {
				data[i] = new QuadraticSpacePerfectHashing<>(tmp[i]);
			}
		}
	}

	/**
	 * On aurait pu modifier la méthode contains afin d'éviter la duplication de
	 * code.<br/>
	 * Ca n'a pas été fait afin de ne pas modifier le code fourni.
	 */
	private int getIndex(AnyType x) {
		int m = data.length;
		int index = ((a * x.hashCode() + b) % p) % m;
		return (index < 0 ? index + m : index);
	}

	public int Size() {
		if (data == null)
			return 0;

		int size = 0;
		for (int i = 0; i < data.length; ++i) {
			size += (data[i] == null ? 1 : data[i].Size());
		}
		return size;
	}

	public boolean contains(AnyType x) {
		if (Size() == 0)
			return false;

		int m = data.length;

		int index = ((a * x.hashCode() + b) % p) % m;

		index = (index < 0 ? index + m : index);

		return ((data[index] != null) && (data[index].contains(x)));
	}
}
