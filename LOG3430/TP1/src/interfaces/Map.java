
package interfaces;

/**
 * Represent a map with six operations available.
 */
public interface Map {
	/**
	 * Add the element in the Map with the given key. <br />
	 * if the element couldn't be added, rehash the Map.
	 */
	public void put(Integer key, Element value);

	/**
	 * Remove an element with the given key. <br />
	 * Return null if not found, the removed element else.
	 */
	public Element remove(Integer key);

	/**
	 * Return the number of elements in the Map.
	 */
	public int size();

	/**
	 * Retrieve an element with the given key. <br />
	 * Return null if not found.
	 */
	public Element get(Integer key);

	/**
	 * Reset the map to the initial state. <br />
	 * Contain no more elements.
	 */
	public void clear();

	/**
	 * Rehash the Map if an element cannot be added.
	 */
	public void rehash();

	/**
	 * Represent an element, i.e. a student represented by it's identifiant and
	 * it's name.
	 */
	public class Element {
		private Integer id;
		private String name;

		public Element() {
		}

		public Element(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public boolean equals(Object o) {
			return this.id == ((Element) o).getId() && this.name == ((Element) o).getName();
		}

		@Override
		public String toString() {
			return "<" + id + "," + name + ">";
		}
	}

}
