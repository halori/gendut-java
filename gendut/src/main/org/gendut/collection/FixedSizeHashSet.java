package org.gendut.collection;

import java.util.NoSuchElementException;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

/**
 * Automatic rehashing is bounded because unscheduled rehashing does not
 * amortize across version histories. The implementation of the underlying table
 * is optimized for sparse tables, hence the default capacity is defined large
 * enough for most practical purposes.<b /> However, there is a simple recipe
 * for those who need growing and shrinking the capacity in a fully-amortized
 * manner: Always keep two disjoint hash sets (a big one and a half-sized small
 * one) around and perform additional move operations for each update so that
 * either the small set is empty when we need to grow or the big set is empty
 * when we need t o shrink.<br />
 * <br />
 * Currently, there are no parameters for controlling when resizing takes place,
 * except a maximum capacity for automatically resized sets. I order to make
 * scheduled preventive copying (see above) feasible, we make the following
 * guaranty: When adding an element, there is no automatically resize if the
 * capacity has achieved its maximum resize-value of if 3*size < capacity. If an
 * element is removed, there is no downsizing if 8*size > capacity.
 * 
 */
public final class FixedSizeHashSet<E> extends ImmutableArrayBase<Object>
		implements UpdatableSet<E, FixedSizeHashSet<E>> {
	/*
	 * Wrapper class for stacks: Without it, it would be impossible to store
	 * stacks in our hash table.
	 */
	private final static class StackWrapper<E> {
		public final Stack<E> stack;

		public StackWrapper(final Stack<E> stack) {
			this.stack = stack;
		}
	}

	private final long size;

	static public <E> FixedSizeHashSet<E> create(long capacity) {
		return new FixedSizeHashSet<E>(capacity);
	}

	public FixedSizeHashSet(long capacity) {
		super(capacity);
		size = 0;
	}

	protected FixedSizeHashSet(ImmutableArrayBase<Object> x, long i, Object e, long newsize) {
		super(x, i, e);
		size = newsize;
	}

	public long capacity() {
		return adressableSize();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FixedSizeHashSet<E> add(E item) {
		long capacity = capacity();
		int hashItem = item.hashCode() % (int) capacity;
		if (hashItem < 0)
			hashItem = -hashItem;
		Object place = get(hashItem);
		if (place == null)
			return new FixedSizeHashSet<E>(this, hashItem, item, size + 1);
		if (place.getClass() == StackWrapper.class) {
			Stack<E> list = ((StackWrapper<E>) place).stack;
			if (list.contains(item))
				return remove(item).add(item);
			else
				return new FixedSizeHashSet<E>(this, hashItem, new StackWrapper(list.push(item)), size + 1);
		} else if (place.equals(item)) {
			return new FixedSizeHashSet<E>(this, hashItem, item, size);
		} else {
			Stack<E> list = Stack.create();
			list = list.push((E) place);
			list = list.push(item);
			return new FixedSizeHashSet<E>(this, hashItem, new StackWrapper(list), size + 1);
		}
	}

	private static class Iterator<E> implements ForwardIterator<E> {
		final ForwardIterator<Object> notNullPlaces;

		Stack<E> list = null;

		public Iterator(FixedSizeHashSet<E> hashSet) {
			notNullPlaces = hashSet.notNullIterator();
		}

		public boolean hasNext() {
			return notNullPlaces.hasNext() || (list != null);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No next element.");
			}
			if (list != null) {
				E item = list.top();
				list = list.pop();
				if (list.size() == 0) {
					list = null;
				}
				return item;
			}
			Object place = notNullPlaces.next();
			if (place.getClass() == StackWrapper.class) {
				list = ((StackWrapper) place).stack;
				E item = list.top();
				list = list.pop();
				if (list.size() == 0) {
					list = null;
				}
				return item;
			}
			E item = (E) place;
			return item;
		}
	}

	public ForwardIterator<E> iterator() {
		return new Iterator<E>(this);
	}

	public Seq<E> seq() {
		return SeqFromIterator.create(iterator());
	}

	@SuppressWarnings("unchecked")
	public E find(E item) {
		int hash = item.hashCode() % (int) capacity();
		if (hash < 0)
			hash = -hash;
		Object place = get(hash);
		if (place == null)
			return null;
		if (place.getClass() == StackWrapper.class) {
			return Collections.findViaIterator(((StackWrapper<E>) place).stack, item);
		}
		if (place.equals(item))
			return (E) place;
		else
			return null;
	}

	public boolean contains(E e) {
		return find(e) != null;
	}

	public long size() {
		return size;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FixedSizeHashSet<E> remove(E item) {
		int hashItem = item.hashCode() % (int) capacity();
		if (hashItem < 0)
			hashItem = -hashItem;
		Object place = get(hashItem);
		if (place == null)
			return this;
		if (place.getClass() == StackWrapper.class) {
			Stack<E> list = ((StackWrapper<E>) place).stack;
			if (!list.contains(item))
				return this;
			else {
				ExtendibleArray<E> top = new ExtendibleArray<E>();
				while (!list.top().equals(item)) {
					top.add(list.top());
					list = list.pop();
				} // `while`
				list = list.pop();
				for (int i = 0; i < top.size(); i++) {
					list = list.push(top.get(i));
				} // `for`
				if (list.size() == 1)
					return new FixedSizeHashSet<E>(this, hashItem, list.top(), size - 1);
				else
					return new FixedSizeHashSet<E>(this, hashItem, new StackWrapper(list), size - 1);
			}
		} else if (place.equals(item)) {
			return new FixedSizeHashSet<E>(this, hashItem, null, size - 1);
		} else {
			return this;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E firstElement() {
		Object place = super.firstElement();
		if (place == null)
			return null;
		if (place.getClass() == StackWrapper.class) {
			Stack<E> list = ((StackWrapper<E>) place).stack;
			return list.first();
		} else
			return (E) place;
	}

	public Int elementCount() {
		return Int.create(size());
	}

	public FixedSizeHashSet<E> clear() {
		return new FixedSizeHashSet<E>(capacity());
	}

	@Override
	public E first() {
		return iterator().next();
	}

	@Override
	public Seq<E> rest() {
		ForwardIterator<E> iterator = iterator();
		iterator.next();
		;
		return SeqFromIterator.create(iterator);
	}

	@Override
	public boolean isEmpty() {
		return elementCount() == Int.ZERO;
	}

	@Override
	public FixedSizeHashSet<E> minus(Set<? extends E> other) {
		// TODO optimize this method
		return Collections.minus(this, other);
	}

	@Override
	public Stream<E> stream() {
		return Collections.stream(this);
	}
}
