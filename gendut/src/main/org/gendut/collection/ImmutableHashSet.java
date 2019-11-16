package org.gendut.collection;

import org.gendut.arithmetic.Int;
import org.gendut.iterator.ConcatIterator;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

final public class ImmutableHashSet<E> extends AbstractCollection<E> implements
		UpdatableSet<E, ImmutableHashSet<E>> {

	private static final int balance = 24;

	private static final double loadfactor = 0.3;

	private static final int minCapacity = 15;

	private final static FixedSizeHashSet<Object> emptyTable = new FixedSizeHashSet<Object>(
			minCapacity);

	@SuppressWarnings("unchecked")
	private <F> FixedSizeHashSet<F> emptyTable() {
		return (FixedSizeHashSet<F>) emptyTable;
	}

	private final FixedSizeHashSet<E> smallSet;

	private final FixedSizeHashSet<E> largeSet;

	@SuppressWarnings("rawtypes")
	private final static ImmutableHashSet emptySet = new ImmutableHashSet();

	@SuppressWarnings("unchecked")
	public static <E> ImmutableHashSet<E> create() {
		return emptySet;
	}

	private ImmutableHashSet() {
		smallSet = emptyTable();
		largeSet = emptyTable();
	}

	private ImmutableHashSet(FixedSizeHashSet<E> A, FixedSizeHashSet<E> B) {
		this.smallSet = A;
		this.largeSet = B;
	}
	
	public static<E> ImmutableHashSet<E> fromSequence(Seq<E> seq) {
		ImmutableHashSet<E> result = create();
		while(!seq.isEmpty()) {
			result = result.add(seq.first());
			seq = seq.rest();
		}
		return result;
	}

	public ImmutableHashSet<E> add(E e) {
		FixedSizeHashSet<E> A = smallSet;
		FixedSizeHashSet<E> B = largeSet;
		if (size() < loadfactor * minCapacity)
			return new ImmutableHashSet<E>(A.add(e), B.remove(e));

		long oldSizeB = B.size();
		if (B == emptyTable())
			B = new FixedSizeHashSet<E>(balance * A.capacity());

		B = B.add(e);
		if (oldSizeB == B.size())
			/*
			 * no change in size means item is overwritten in B and did not
			 * exist in A:
			 */
			return new ImmutableHashSet<E>(A, B);

		A = A.remove(e);
		if (2 * A.size() > loadfactor * B.capacity() - B.size()) {
			/*
			 * We don't use iterators here: They are expensive if they are gc-ed
			 * lately.
			 */
			for (int i = 0; i < 2; i++) {
				E f = A.firstElement();
				if (f == null)
					break;
				A = A.remove(f);
				B = B.add(f);
			}// ` for`
		}// ` if`
		if (A.size() == 0) {
			A = B;
			B = new FixedSizeHashSet<E>(balance * A.size());
		}
		return new ImmutableHashSet<E>(A, B);
	}

	public ImmutableHashSet<E> remove(E e) {
		FixedSizeHashSet<E> A = smallSet;
		FixedSizeHashSet<E> B = largeSet;
		A = A.remove(e);
		B = B.remove(e);
		if (B.size() < loadfactor * A.capacity() - A.size()) {
			E f = B.firstElement();
			if (f != null) {
				B = B.remove(f);
				A = A.add(f);
			}
		}

		if (B.size() == 0) {
			if (A.size() < minCapacity * loadfactor)
				B = emptyTable();
			else {
				B = A;
				long newACapacity = B.size() / balance;
				if (newACapacity < minCapacity)
					newACapacity = minCapacity;
				A = new FixedSizeHashSet<E>(newACapacity);
			}
		}
		return new ImmutableHashSet<E>(A, B);
	}

	public boolean contains(E e) {
		if (smallSet.size() > largeSet.size())
			return smallSet.contains(e) || largeSet.contains(e);
		else
			return largeSet.contains(e) || smallSet.contains(e);
	}

	public ForwardIterator<E> iterator() {
		ConstantArray<ForwardIterator<E>> it = ConstantArray.pair(
				smallSet.iterator(), largeSet.iterator());
		return new ConcatIterator<E>(it);
	}

	public Seq<E> seq() {
		return SeqFromIterator.create(iterator());
	}

	public long size() {
		return smallSet.size() + largeSet.size();
	}

	public E find(E item) {
		FixedSizeHashSet<E> A = smallSet;
		FixedSizeHashSet<E> B = largeSet;
		if (smallSet.size() < largeSet.size()) {
			FixedSizeHashSet<E> tmp = A;
			A = B;
			B = tmp;
		}
		E same = A.find(item);
		if (same != null)
			return same;
		else
			return B.find(item);
	}

	@Override
	public int hashCode() {
		return Collections.hashCodeForSet(this);
	}

	@Override
	public boolean equals(Object obj) {
		return Collections.equalsForSets(this, obj);
	}

	@Override
	public String toString() {
		return Collections.toStringSorted(this);
	}

	long capacity() {
		throw new IllegalStateException("unrestricted capacity.");
	}

	public Int elementCount() {
		return Int.create(size());
	}

	@SuppressWarnings("unchecked")
	public ImmutableHashSet<E> clear() {
		return emptySet;
	}

	@Override
	public ImmutableHashSet<E> minus(Set<? extends E> other) {
		// TODO optimize this method
		return Collections.minus(this, other);
	}
}
