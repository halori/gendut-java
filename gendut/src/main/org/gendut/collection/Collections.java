package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.errors.Assertions;
import org.gendut.func.Function;
import org.gendut.func.Pair;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IterableCollection;
import org.gendut.iterator.IteratorFromSeq;
import org.gendut.iterator.LookAheadIterator;
import org.gendut.seq.FlattenedSeq;
import org.gendut.seq.MergedSortedSeqs;
import org.gendut.seq.Seq;

// ! Routines for Collections
/* <literate> */
/**
 * This class provides several routines for dealing with collections
 **/
final public class Collections {
	/**
	 * This class has no instances.
	 */
	private Collections() {
	}

	public static boolean containsViaIterator(Seq<?> collection, Object e) {
		ForwardIterator<?> it = collection.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item == null) {
				if (e == null)
					return true;
			} else if (item.equals(e))
				return true;
		}
		return false;
	}

	/**
	 * Contains an element equal to <i>e</i>, if such an element exists.
	 * Otherwise, null is returned. It is required that <i>e</i> is not null.
	 */
	public static <E> E findViaIterator(Seq<E> collection, E e) {
		if (e == null)
			throw new NullPointerException("Cannot search for null element");
		ForwardIterator<E> it = collection.iterator();
		while (it.hasNext()) {
			E item = it.next();
			if (item.equals(e))
				return item;
		} // `for`
		return null;
	}

	/**
	 * turns a sequence into an immutable collection
	 */
	public static <E> ImmutableHashSet<E> createSet(Seq<E> seq) {
		ImmutableHashSet<E> set = ImmutableHashSet.create();
		ForwardIterator<E> it = seq.iterator();
		while (it.hasNext()) {
			set = set.add(it.next());
		}
		return set;
	}

	final static class SingletonSet<E> extends AbstractCollection<E>implements ImmutableSet<E> {
		final E element;

		public SingletonSet(E e) {
			if (e == null)
				throw new NullPointerException("Singleton sets don't allow null elements");
			this.element = e;
		}

		public long size() throws IndexOutOfBoundsException {
			return 1;
		}

		public Int elementCount() {
			return Int.ONE;
		}

		public boolean contains(E e) {
			return element.equals(e);
		}

		public E find(E e) {
			if (contains(e))
				return element;
			else
				return null;
		}

		public ForwardIterator<E> iterator() {
			Stack<E> s = Stack.create();
			return s.push(element).iterator();
		}

		@Override
		public int hashCode() {
			return hashCodeForSet(this);
		}

		@Override
		public boolean equals(Object obj) {
			return equalsForSets(this, obj);
		}

		@Override
		public String toString() {
			return toStringIterationOrder(this);
		}
	}

	public static <E> ImmutableSet<E> singleton(E e) {
		return new SingletonSet<E>(e);
	}

	/**
	 * Performs a default equality test between a set and another object. Use
	 * this or a functionally equivalent method in all implementations of the
	 * set interface.
	 */
	@SuppressWarnings("unchecked")
	public static <E> boolean equalsForSets(Set<E> a, Object obj) {
		if (a == obj) {
			return true;
		}
		if ((a == null) || (obj == null))
			return false;
		if (!(obj instanceof Set)) {
			return false;
		}
		Set<E> b = (Set<E>) obj;
		if (a.size() != b.size())
			return false;
		ForwardIterator<? extends E> iterA = a.iterator();
		while (iterA.hasNext())
			if (!b.contains((E) iterA.next()))
				return false;
		ForwardIterator<? extends E> iterB = b.iterator();
		while (iterB.hasNext())
			if (!a.contains((E) iterB.next()))
				return false;
		return true;
	}

	/**
	 * Performs a default equality test between a list and another object. Use
	 * this or a functionally equivalent method in all implementations of the
	 * list interface.
	 */
	@SuppressWarnings("unchecked")
	public static <E> boolean equalsForList(ImmutableCollection<E> c, Object obj) {
		if (c == obj) {
			return true;
		}
		if ((c == null) || (obj == null))
			return false;
		if (!(obj instanceof List)) {
			return false;
		}
		List<Object> d = (List<Object>) obj;
		if (c.size() != d.size()) {
			return false;
		}
		long n = c.size();
		ForwardIterator<E> it1 = c.iterator();
		ForwardIterator<Object> it2 = d.iterator();
		for (int i = 0; i < n; i++) {
			Object o1 = it1.next();
			Object o2 = it2.next();
			if (o1 != o2) {
				if ((o1 == null) || (o2 == null) || (!o1.equals(o2)))
					return false;
			}
		}
		return true;
	}

	/**
	 * Computes a default hash value of a set. Use this or a functionally
	 * equivalent method in all implementations of the set interface.
	 */
	final public static <E> int hashCodeForSet(IterableCollection<E> c) {
		ForwardIterator<E> it = c.iterator();
		int s = 0;
		while (it.hasNext()) {
			Object value = it.next();
			if (value != null)
				s = s + value.hashCode();
		}
		return s;
	}

	/**
	 * Computes a default hash value of a list. Use this or a functionally
	 * equivalent method in all implementations of the list interface.
	 */
	final public static <E> int hashCodeForLists(IterableCollection<E> c) {
		ForwardIterator<E> it = c.iterator();
		int s = 0;
		while (it.hasNext()) {
			Object value = it.next();
			int x = 0;
			if (value != null)
				x = value.hashCode();
			s = 12343 * s + x;
		}
		return s;
	}

	/**
	 * Returns a string representation of the given collection with elements in
	 * iteration order. Use this if the collection is sorted (tree) or has a
	 * deterministic order (stack).
	 */
	public static <E> String toStringIterationOrder(Seq<E> collection) {
		StringBuffer str = new StringBuffer();
		str.append("[");
		ForwardIterator<E> it = collection.iterator();
		boolean isFirst = true;
		while (it.hasNext()) {
			if (isFirst)
				isFirst = false;
			else
				str.append(", ");
			E item = it.next();
			if (item == null)
				str.append("<null>");
			else
				str.append(item.toString());
		} // `for`
		str.append("]");
		return str.toString();
	}

	/**
	 * Returns a string representation of the given collection with element
	 * strings in lexicograhic order. Use this if the collection has an
	 * unpredictable iteration order (hashset).
	 */
	public static <E> String toStringSorted(ImmutableCollection<E> set) {
		Stack<String> elementStr = Stack.create();
		ForwardIterator<E> it = set.iterator();
		while (it.hasNext()) {
			E item = it.next();
			if (item == null)
				elementStr = elementStr.push("<null>");
			else
				elementStr = elementStr.push(item.toString());
		}
		elementStr = Stack.fromCollection(sort(elementStr, (a,b) -> a.compareTo(b)));
		StringBuffer str = new StringBuffer();
		str.append("[");
		ForwardIterator<String> elementStrIt = elementStr.iterator();
		for (int i = 0; i < set.size(); i++) {
			str.append(elementStrIt.next());
			if (i < set.size() - 1)
				str.append(", ");
		} // `for`
		str.append("]");
		return str.toString();
	}

	/**
	 * Returns a list with the same elements in sorted order. The returned list
	 * is guaranteed to have O(1) random access time. The sorting algorithm
	 * needs O(log n) time and O(log n) stack size. Both limits are maximum, not
	 * average.
	 */
	public static <E> Array<E> sort(ImmutableCollection<E> C, Comparator<? super E> cmp) {
		Array<E> list = sort(C, cmp, SORT_LIST_IMPL_THRESHOLD);
		if (C.size() < SORT_LIST_IMPL_THRESHOLD)
			return list;
		else
			return ImmutableArray.fromCollection(list);
	}

	/**
	 * Returns a list with the same elements in sorted order, but doubles
	 * eliminated. Doubles are identified by the order relation, not via equals.
	 * The returned list is guaranteed to have O(1) random access time. The
	 * sorting algorithm needs O(log n) time and O(log n) stack size. Both
	 * limits are maximum, not average.
	 */
	public static <E> Array<E> sortUnique(ImmutableCollection<E> C, Comparator<? super E> cmp) {
		Array<E> list = sort(C, cmp, SORT_LIST_IMPL_THRESHOLD);
		long n = list.size();
		if (n <= 1)
			return list;
		ExtendibleArray<E> uniqueList = new ExtendibleArray<E>();
		ForwardIterator<E> it = list.iterator();
		E last = it.next();
		uniqueList.add(last);
		while (it.hasNext()) {
			E next = it.next();
			if (cmp.compare(next, last) != 0) {
				uniqueList.add(next);
				last = next;
			}
		}
		if (C.size() < SORT_LIST_IMPL_THRESHOLD)
			return uniqueList.asConstant();
		else
			return ImmutableArray.fromCollection(list);
	}

	private static final int SORT_LIST_IMPL_THRESHOLD = 50 * 1024 * 1024;

	/**
	 * Returns a list with the same elements in sorted order. If the number of
	 * elements is small enough (less than the given threshold) the returned
	 * list is guaranteed to have the access time of an array (with a small
	 * constant factor for one additional method call). If the size is larger
	 * than the given threshold, then the list representation may have linear
	 * access time! The threshold must be in the range 0..Integer.MAX_VALUE/2-1
	 * The sorting algorithm needs O(log n) time and O(log n) stack size. Both
	 * limits are maximum, not average.
	 */
	public static <E> Array<E> sort(ImmutableCollection<E> C, Comparator<? super E> cmp, int threshold) {
		if ((threshold < 0) || (threshold >= Integer.MAX_VALUE))
			throw new IllegalArgumentException(
					"threshold for list implementation " + "in sorter" + "must be within 0..Integer.MAX_VALUE/2-1");
		long n = C.size();
		if (n <= 1)
			return ConstantArray.fromCollection(C);
		if (n <= threshold)
			return ConstantArray.fromCollection(C).sort(cmp);
		long p = n / 2;
		Array<E> S = ConstantArray.fromCollection(C);
		Array<E> left = sort(Range.create(S, 0, p), cmp, threshold);
		Array<E> right = sort(Range.create(S, p, n), cmp, threshold);
		Array<E> L = merge(left, right, cmp);
		return L;
	}

	/**
	 * Merge two sorted arrays into a new sorted list (eager).
	 */
	static <E> Array<E> merge(Seq<E> left, Seq<E> right, Comparator<? super E> cmp) {
		long size = size(left) + size(right);
		if (size < Integer.MAX_VALUE / 4) {
			ExtendibleArray<E> S = new ExtendibleArray<E>();
			LookAheadIterator<E> it1 = new LookAheadIterator<E>(left.iterator());
			LookAheadIterator<E> it2 = new LookAheadIterator<E>(right.iterator());
			while (it1.hasNext() && it2.hasNext()) {
				if (cmp.compare(it1.peek(), it2.peek()) <= 0)
					S.add(it1.next());
				else
					S.add(it2.next());
			}
			if (!it1.hasNext())
				it1 = it2;
			while (it1.hasNext()) {
				S.add(it1.next());
			}
			return S.asConstant();
		} else {
			ImmutableArray<E> S = ImmutableArray.create(size);
			int pos = 0;
			LookAheadIterator<E> it1 = new LookAheadIterator<E>(left.iterator());
			LookAheadIterator<E> it2 = new LookAheadIterator<E>(right.iterator());
			while (it1.hasNext() && it2.hasNext()) {
				if (cmp.compare(it1.peek(), it2.peek()) <= 0)
					S.set(pos++, it1.next());
				else
					S.set(pos++, it2.next());
			}
			if (!it1.hasNext())
				it1 = it2;
			while (it1.hasNext()) {
				S.set(pos++, it1.next());
			}
			Assertions.assertEquals("Position and size should be the same", size, pos);
			return S;

		}

	}

	public static long size(Seq<?> seq) {
		if (seq instanceof ImmutableCollection) {
			return ((ImmutableCollection<?>) seq).size();
		} else {
			long s = 0;
			ForwardIterator<?> it = seq.iterator();
			while (it.hasNext()) {
				s = s + 1;
				it.next();
				if (s == Long.MAX_VALUE) {
					throw new IllegalStateException("Size is not a long value.");
				}
			}
			return s;
		}

	}

	public static Int count(Seq<?> seq) {
		if (seq instanceof ImmutableCollection) {
			return ((ImmutableCollection<?>) seq).elementCount();
		} else {
			Int s = Int.ZERO;
			ForwardIterator<?> it = seq.iterator();
			while (it.hasNext()) {
				s = s.add(Int.ONE);
				it.next();
			}
			return s;
		}
	}

	/**
	 * Merge two sorted sequences into a new sorted sequence (lazy).
	 */
	static <E> Seq<E> mergeLazy(Seq<E> left, Seq<E> right, Comparator<? super E> cmp) {
		ExtendibleArray<Seq<E>> seqs = new ExtendibleArray<Seq<E>>();
		seqs.add(left);
		seqs.add(right);
		return MergedSortedSeqs.create(seqs.asConstant(), cmp);
	}

	public static <E> E[] toArray(Seq<? extends E> list, E[] result) {
		ExtendibleArray<E> array = new ExtendibleArray<E>();
		ForwardIterator<? extends E> it = list.iterator();
		while (it.hasNext())
			array.add(it.next());
		return array.toArray(result);
	}

	public static <M, N> MonoidMap<M, N> createHom(final Function<M, N> map, final Function<Pair<N, N>, N> add,
			final N zero) {
		return new MonoidMap<M, N>() {
			public N add(N x, N y) {
				return add.get(new Pair<N, N>(x, y));
			}

			public N map(M a) {
				return map.get(a);
			}

			public N zero() {
				return zero;
			}
		};
	}

	public static <T> boolean isDisjoint(ImmutableSet<T> c1, ImmutableSet<T> c2) {
		if (c1.size() == 0 || c2.size() == 0)
			return true;
		if (c1 == c2)
			return false;
		if (c1.size() > c2.size()) {
			ImmutableSet<T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}
		ForwardIterator<T> it = c1.iterator();
		while (it.hasNext()) {
			T e = it.next();
			if (c2.contains(e))
				return false;
		}
		return true;
	}

	/**
	 * The union of two immutably updatable sets.
	 */
	public static <E, S extends UpdatableSet<E, S>> S merge(S a, S b) {
		if (a.size() > b.size())
			return merge(b, a);
		else {
			ForwardIterator<E> it = b.iterator();
			while (it.hasNext()) {
				a = a.add(it.next());
			}
			return a;
		}
	}

	/**
	 * creates the intersection of two sets.
	 */
	public static <E> ImmutableHashSet<E> intersection(ImmutableSet<E> a, ImmutableSet<E> b) {
		if (a.size() > b.size())
			return intersection(b, a);
		ImmutableHashSet<E> result = ImmutableHashSet.create();
		ForwardIterator<E> it = a.iterator();
		while (it.hasNext()) {
			E e = it.next();
			if (b.contains(e))
				result = result.add(e);
		}
		return result;
	}

	static public class RandomAccessIterator<E> implements ForwardIterator<E> {
		private int i = 0;

		private Array<E> collection;

		public RandomAccessIterator(Array<E> collection) {
			this.collection = collection;
		}

		public boolean hasNext() {
			return i < collection.size();
		}

		public E next() {
			return collection.get(i++);
		}
	}

	static public <F, E extends F, R extends ImmutableCollection<E>> Comparator<R> lexicographicOrder(
			final Comparator<F> cmp) {
		return new Comparator<R>() {
			public int compare(R a, R b) {
				if (a == b)
					return 0;
				ForwardIterator<E> it1 = a.iterator();
				ForwardIterator<E> it2 = b.iterator();
				while (it1.hasNext() && it2.hasNext()) {
					E e1 = it1.next();
					E e2 = it2.next();
					int c = cmp.compare(e1, e2);
					if (c != 0)
						return c;
				}
				if (it1.hasNext())
					return -1;
				else if (it2.hasNext())
					return 1;
				else
					return 0;
			}
		};
	}

	public static <E> Array<E> distinct(IterableCollection<? extends E> list) {
		ExtendibleArray<E> setArray = new ExtendibleArray<E>();
		ImmutableHashSet<E> setHash = ImmutableHashSet.create();
		ForwardIterator<? extends E> it = list.iterator();
		while (it.hasNext()) {
			E next = it.next();
			if (!setHash.contains(next)) {
				setHash = setHash.add(next);
				setArray.add(next);
			}
		}
		return setArray.asConstant();
	}

	public static <E> Seq<E> skip(Seq<E> seq, long n) {
		ForwardIterator<E> it = seq.iterator();
		while (n > 0 && it.hasNext()) {
			n--;
			it.next();
		}
		return seq;
	}

	public static <E> Seq<E> limit(Seq<E> seq, long maxSize) {
		return new SeqWithLimit<E>(seq, maxSize);
	}

	private static class SeqWithLimit<E> extends AbstractList<E>implements Seq<E> {
		private final Seq<E> seq;
		private final long maxSize;

		public SeqWithLimit(Seq<E> seq, long maxSize) {
			this.seq = seq;
			this.maxSize = maxSize;
		}

		@Override
		public ForwardIterator<E> iterator() {
			return new IteratorFromSeq<E>(this);
		}

		@Override
		public E first() {
			Assertions.assertion("Sequence must not e empty", !isEmpty());
			return seq.first();
		}

		@Override
		public Seq<E> rest() {
			Assertions.assertion("Sequence must not e empty", !isEmpty());
			return new SeqWithLimit<E>(seq, maxSize - 1);
		}

		@Override
		public boolean isEmpty() {
			return maxSize > 0;
		}
	}

	public static <E> E reduce(Seq<E> seq, BinaryFunction<E, E, E> op, E e) {
		ForwardIterator<E> it = seq.iterator();
		while (it.hasNext()) {
			E x = it.next();
			Assertions.assertNotNull("null elements cannot be reduced.", x);
			if (e == null)
				e = x;
			else
				e = op.get(e, x);
		}
		return e;
	}

	public static <M, S extends UpdatableSet<M, S>> S minus(S a, Set<? extends M> b) {
		ForwardIterator<? extends M> it = b.iterator();
		while (it.hasNext())
			a = a.remove(it.next());
		return a;
	}

	public static <E> Stream<E> stream(Seq<E> seq) {
		return new Stream<E>(seq);
	}

	public static <E> long indexOf(Seq<E> seq, E e) {
		int i = 0;
		ForwardIterator<E> it = seq.iterator();
		while (it.hasNext()) {
			if (e.equals(it.next()))
				return i;
			i++;
		}
		return -1;
	}

	public static <E> Seq<E> concat(Seq<Seq<E>> seqs) {
		return FlattenedSeq.create(seqs);
	}
}
