package org.gendut.collection;

import org.gendut.algorithm.Comparator;
import org.gendut.arithmetic.Int;

public final class SortedSet<E> extends CatenableArrayTree<E> implements
		UpdatableSet<E, SortedSet<E>>, CatenableArray<E> {

	public SortedSet(Object root, Comparator<? super E> cmp) {
		super(root, null, cmp);
	}

	<F> SortedSet(Object root,
			ImmutableHashMap<MonoidMap<E, ?>, Object> images,
			Comparator<? super E> cmp) {
		super(root, images, cmp);
	}

	public SortedSet(CatenableArrayTree<E> arr1, CatenableArrayTree<E> arr2) {
		super(arr1, arr2);
		if (cmp.compare(arr1.last(), arr2.first()) >= 0)
			throw new IllegalArgumentException(
					"Sorted sets cannot be concatenated.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public <E> SortedSet<E> create(Comparator<? super E> cmp) {
		return new SortedSet(emptyTree, cmp);
	}

	public SortedSet<E> add(E e) {
		return new SortedSet<E>(insert(root, cmp, e,
				SortedCollection.setBehavior), cmp);
	}

	public SortedSet<E> remove(E e) {
		return new SortedSet<E>(remove(root, cmp, e), cmp);
	}

	public SortedSet<E> clear() {
		return new SortedSet<E>(emptyTree, emptyImages(images.keys()), cmp);
	}

	public SortedSet<E> catenate(SortedSet<? extends E> array) {
		return (SortedSet<E>) super.catenate(array);
	}

	public SortedCollection<E> catenate(SortedCollection<? extends E> array) {
		return (SortedCollection<E>) super.catenate(array);
	}

	public LargeArray<E> catenate(LargeArray<? extends E> array) {
		return (LargeArray<E>) super.catenate(array);
	}

	public SortedSet<E> subArray(Int start, Int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedSet<E> insertAt(Int pos, E e) {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedSet<E> replaceAt(Int pos, E e) {
		if (pos.compareTo(0) < 0 || pos.compareTo(elementCount()) >= 0)
			throw new IndexOutOfBoundsException();

		if (pos.compareTo(0) >= 0 && cmp.compare(get(pos.subtract(1)), e) >= 0)
			throw new IllegalArgumentException(
					"Predecessor is not smaller than new element.");
		if (pos.compareTo(elementCount()) < 0
				&& cmp.compare(e, get(pos.add(1))) >= 0)
			throw new IllegalArgumentException(
					"Successor is not larger than new element.");

		return new SortedSet<E>(replace(root, pos, e), cmp);
	}

	public boolean contains(E e) {
		return findFirst(root, cmp, e) != noElementFound;
	}

	@SuppressWarnings("unchecked")
	public E find(E e) {
		Object found = findFirst(root, cmp, e);
		if (found == noElementFound)
			return null;
		return (E) found;
	}

	final public Int lastOf(E e) {
		// TODO Auto-generated method stub
		return null;
	}

	final public Int firstOf(E e) {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedSet<E> removeAt(Int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedSet<E> insertAt(long pos, E e) {
		return insertAt(Int.create(pos), e);
	}

	public SortedSet<E> removeAt(long pos) {
		return removeAt(Int.create(pos));
	}

	public SortedSet<E> replaceAt(long pos, E e) {
		return replaceAt(Int.create(pos), e);
	}

	public SortedSet<E> subArray(long start, long end) {
		return subArray(Int.create(start), Int.create(end));
	}

	public SortedSet<E> addMap(MonoidMap<E, ?> map) {
		if (images.containsKey(map))
			return this;
		else
			return new SortedSet<E>(root, images.put(map, computeImage(map)),
					cmp);
	}

	public SortedSet<E> removeMap(MonoidMap<E, ?> map) {
		return new SortedSet<E>(root, images.remove(map), cmp);
	}

	@Override
	public SortedSet<E> minus(Set<? extends E> other) {
		// TODO optimize this method
		return Collections.minus(this, other);
	}
}
