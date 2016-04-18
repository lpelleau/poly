
package interfaces;

import interfaces.Map.Element;

/**
 * Represent a list with five operations available.
 */
public interface List {
	/**
	 * Add the element in the List. <br />
	 * If the element couldn't be added, <b>false</b> is returned, <b>true</b>
	 * else.
	 */
	public boolean add(Element e);

	/**
	 * Retrieve an element with the given key. <br />
	 * Return null if not found.
	 */
	public Element get(Integer key);

	/**
	 * Remove an element with the given key. <br />
	 * Return null if not found, the removed element else.
	 */
	public Element remove(Integer key);

	/**
	 * Remove the first element of the List. <br />
	 * Return null if the List is empty, the removed element else.
	 */
	public Element remove();

	/**
	 * Return the number of elements in the List.
	 */
	public int size();
}