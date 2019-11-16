package org.gendut.iterator;

import java.util.NoSuchElementException;

import org.gendut.seq.Seq;

public class ConcatIterator<E> implements ForwardIterator<E> {

	final ForwardIterator<? extends ForwardIterator<E>> iterators;
	ForwardIterator<E> current = null;

	public ConcatIterator(Seq<? extends ForwardIterator<E>> iterators) {
		this.iterators = iterators.iterator();
	}

	public boolean hasNext() {
		while (current == null || !current.hasNext()) {
			if (!iterators.hasNext())
				return false;
			current = iterators.next();
		}
		return true;
	}

	public E next() {
		if (!hasNext())
			throw new NoSuchElementException();
		else
			return current.next();
	}
}
