package org.gendut.collection.mutable;

import org.gendut.collection.ImmutableHashSet;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

//! Mutable Hashset
/* <literate> */
/**
 * This mutable hashmap is a helper class. It serves as a replacement for the
 * hashmap implementation from the Java collections, which is not present in
 * Java ME.<br />
 * The stored values must not be null. Furthermore, their equal-methods must be
 * consistent to their hash codes.
 */
public final class MutableHashSet<E> {

	/*
	 * The implementation is backed by a map. All operations delegate to the
	 * map.
	 */
	private MutableHashMap<E, Object> asMap = null;

	final static int DEFAULT_INITIAL_CAPACITY = MutableHashMap.DEFAULT_INITIAL_CAPACITY;

	public MutableHashSet() {
		asMap = new MutableHashMap<E, Object>(DEFAULT_INITIAL_CAPACITY);
	}

	public MutableHashSet(long capacity) {
		asMap = new MutableHashMap<E, Object>(capacity);
	}

	public boolean contains(E e) {
		return asMap.containsKey(e);
	}

	public ForwardIterator<E> iterator() {
		return asMap.keyIterator();
	}

	public Seq<E> seq() {
		return SeqFromIterator.create(iterator());
	}

	public long size() {
		return asMap.size();
	}

	public void add(E e) {
		asMap.put(e, null);
	}

	public void remove(E e) {
		asMap.remove(e);
	}

	public E find(E item) {
		return asMap.findKey(item);
	}

	public ImmutableHashSet<E> asConstant() {
		ImmutableHashSet<E> result = ImmutableHashSet.create();
		ForwardIterator<E> it = iterator();
		while (it.hasNext()) {
			result = result.add(it.next());
		}
		return result;

	}

	@Override
	public String toString() {
		return asConstant().toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other.getClass() != MutableHashSet.class))
			return false;
		return this.asMap.equals(((MutableHashSet<?>) other).asMap);
	}

	@Override
	public int hashCode() {
		return this.asMap.hashCode();
	}
}// `class`
