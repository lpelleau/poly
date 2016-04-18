import java.util.ArrayList;

public interface PriorityQueue<AnyType> {

	int size();

	void clear();

	boolean isEmpty();

	boolean contains(AnyType x);

	AnyType poll();

	boolean add(AnyType x, int priority);

	void updatePriority(AnyType x, int priority);

	ArrayList<AnyType> getMax();
}
