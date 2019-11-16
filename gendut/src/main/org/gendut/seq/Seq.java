package org.gendut.seq;

import org.gendut.iterator.IterableCollection;

// !Sequence Interface
/* <literate> */
/**
 * Interface for sequences. There is no addintional contract for equals(),
 * hashcode(), and toString()
 */
public interface Seq<E> extends IterableCollection<E> {
	/**
	 * returns the sequence's first element. Raises an exception if the sequence
	 * is empty.
	 */
	E first();

	/**
	 * returns the rest of the sequence, that is, all elements (in order) except
	 * the first. Raises an exception if the sequence is empty.
	 */
	Seq<E> rest();

	boolean isEmpty();

}// `interface`
