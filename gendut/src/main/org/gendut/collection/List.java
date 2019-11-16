package org.gendut.collection;

public interface List<E> extends ImmutableCollection<E> {
	/**
	 * Two lists are equal if and only if they they produce element-wise equal
	 * sequences in iteration order
	 */
	public boolean equals(Object obj);

	/**
	 * The hashcode of a set the sum of the hashcodes of its elements.
	 */
	public int hashCode();

	/**
	 * Different implementations of this interface are allowed to have different
	 * string representations. However, the string representation must be
	 * independent from platform-specific behavior and it must be reproducable
	 * across different runs of the same program.
	 */
	public String toString();

	/**
	 * Returns the stored representation of the given item.
	 */
	public E find(E item);
}
