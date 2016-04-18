public class PQEntry<T> {

	T value;
	int priority;

	public PQEntry(T value, int priority) {
		this.value = value;
		this.priority = priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String toString() {
		return "{" + value + ", " + priority + "}";
	}

}
