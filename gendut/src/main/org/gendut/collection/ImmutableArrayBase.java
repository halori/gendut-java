package org.gendut.collection;

import java.util.NoSuchElementException;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.errors.Assertions;
import org.gendut.iterator.ForwardIterator;

// Immutable Tuples Base Class
/* <literate> */
/**
 * This class is package-private. It is used for the implementation of immutable
 * array-based data structures. A Tree-tuple impements a list of element. The
 * number of elements is fixed. Replacement of an element produces a new tuple
 * in logarithmic time. <br />
 * <br />
 * Typically, this base class is used for the implementation of immutable lists
 * of fixed length, or immutable hashtables or queues. The underlying
 * implementation is a balanced tree with nodes of large arity, the height of
 * the tree is small.
 */
class ImmutableArrayBase<E> {
	static private final int ds = 4;

	static private final int arity = 16;

	private final long size;

	private final Object t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15;

	public long adressableSize() {
		return size;
	}

	/**
	 * constructs a tuple with a given size All elements are null. When the
	 * constructor is called, actually no tree is constructed.
	 */
	public ImmutableArrayBase(long size) {
		if (size < 0)
			throw new IllegalArgumentException("size < 0: " + size);
		this.size = size;
		t0 = t1 = t2 = t3 = t4 = t5 = t6 = t7 = t8 = t9 = t10 = t11 = t12 = t13 = t14 = t15 = null;

	}

	private ImmutableArrayBase(long size, Object e0, Object e1, Object e2, Object e3, Object e4, Object e5, Object e6,
			Object e7, Object e8, Object e9, Object e10, Object e11, Object e12, Object e13, Object e14, Object e15) {
		if (size < 0)
			throw new IllegalArgumentException("size < 0: " + size);
		this.size = size;
		t0 = e0;
		t1 = e1;
		t2 = e2;
		t3 = e3;
		t4 = e4;
		t5 = e5;
		t6 = e6;
		t7 = e7;
		t8 = e8;
		t9 = e9;
		t10 = e10;
		t11 = e11;
		t12 = e12;
		t13 = e13;
		t14 = e14;
		t15 = e15;
	}

	protected E get(long i) {

		if ((i < 0) || (i >= size)) {
			throw new IndexOutOfBoundsException("index is not between 0 and " + size + ".");
		}

		return get(this, i);
	}

	@SuppressWarnings("unchecked")
	static private final <E> E get(ImmutableArrayBase<E> tree, long i) {
		long capacity = arity;
		while (capacity < tree.size) {
			capacity = capacity << ds;
		}

		while (tree != null) {
			long k = (i * arity) / capacity;
			Object t = null;
			switch ((int) k) {
			case 0:
				t = tree.t0;
				break;
			case 1:
				t = tree.t1;
				break;
			case 2:
				t = tree.t2;
				break;
			case 3:
				t = tree.t3;
				break;
			case 4:
				t = tree.t4;
				break;
			case 5:
				t = tree.t5;
				break;
			case 6:
				t = tree.t6;
				break;
			case 7:
				t = tree.t7;
				break;
			case 8:
				t = tree.t8;
				break;
			case 9:
				t = tree.t9;
				break;
			case 10:
				t = tree.t10;
				break;
			case 11:
				t = tree.t11;
				break;
			case 12:
				t = tree.t12;
				break;
			case 13:
				t = tree.t13;
				break;
			case 14:
				t = tree.t14;
				break;
			case 15:
				t = tree.t15;
				break;
			}// `switch`
			if ((capacity == arity) || (t == null)) {
				return (E) t;
			} else {
				tree = (ImmutableArrayBase<E>) t;
				capacity = capacity >> ds;
				i = i - k * capacity;
			} // `else`
		} // `while`
		return null;
	}

	public ConstantArray<Int> differentPositions(ImmutableArrayBase<E> other) {
		if (other.size != this.size)
			throw new IllegalArgumentException("Other array must have the same size");
		ExtendibleArray<Int> diffPos = new ExtendibleArray<Int>();
		long capacity = arity;
		while (capacity < size) {
			capacity = capacity << ds;
		}
		collectDifferentPositions(0, capacity, this, other, diffPos);
		return diffPos.asConstant();
	}

	@SuppressWarnings("unchecked")
	private void collectDifferentPositions(long start, long size, Object A, Object B,
			ExtendibleArray<Int> diffPos) {
		if (A == B)
			return;
		if (size == 1) {
			if (!A.equals(B)) {
				diffPos.add(Int.create(start));
			}
			return;
		}
		
		long sizeSteps = size >> ds;
		
		if (A == null || B == null) {
			collectNonNullPositions(start,size, A != null ? A : B, diffPos);
			return;
		}
		
	    ImmutableArrayBase<E> treeA = (ImmutableArrayBase<E>) A;
	    ImmutableArrayBase<E> treeB = (ImmutableArrayBase<E>) B;
			
		collectDifferentPositions(start, sizeSteps, treeA.t0, treeB.t0, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t1, treeB.t1, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t2, treeB.t2, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t3, treeB.t3, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t4, treeB.t4, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t5, treeB.t5, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t6, treeB.t6, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t7, treeB.t7, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t8, treeB.t8, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t9, treeB.t9, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t10, treeB.t10, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t11, treeB.t11, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t12, treeB.t12, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t13, treeB.t13, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t14, treeB.t14, diffPos);
		start += sizeSteps;
		collectDifferentPositions(start, sizeSteps, treeA.t15, treeB.t15, diffPos);
	}


	private void collectNonNullPositions(long start, long size, Object A, ExtendibleArray<Int> diffPos) {
		if (A == null)
			return;
		if (size == 1) {
			diffPos.add(Int.create(start));
			return;
		}
		
		long sizeSteps = size >> ds;
		
		ImmutableArrayBase<?> tree = (ImmutableArrayBase<?>) A;
		
		collectNonNullPositions(start, sizeSteps, tree.t0, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t1, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t2, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t3, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t4, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t5, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t6, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t7, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t8, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t9, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t10, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t11, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t12, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t13, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t14, diffPos);
		start += sizeSteps;
		collectNonNullPositions(start, sizeSteps, tree.t15, diffPos);
	}

	/*
	 * constructs a new array from another by replacing one element.
	 */
	public ImmutableArrayBase(ImmutableArrayBase<E> x, long i, E e) {

		if ((i < 0) || (i >= x.size)) {
			throw new IndexOutOfBoundsException("Index: " + i);
		}

		size = x.size;

		long capacity = 1;
		while (capacity < size) {
			capacity = capacity << ds;
		}

		Object t = e;
		long k = (i * arity) / capacity;

		Object e0 = x.t0, e1 = x.t1, e2 = x.t2, e3 = x.t3, e4 = x.t4, e5 = x.t5, e6 = x.t6, e7 = x.t7, e8 = x.t8,
				e9 = x.t9, e10 = x.t10, e11 = x.t11, e12 = x.t12, e13 = x.t13, e14 = x.t14, e15 = x.t15;

		if (size > arity) {
			switch ((int) k) {
			case 0:
				t = e0;
				break;
			case 1:
				t = e1;
				break;
			case 2:
				t = e2;
				break;
			case 3:
				t = e3;
				break;
			case 4:
				t = e4;
				break;
			case 5:
				t = e5;
				break;
			case 6:
				t = e6;
				break;
			case 7:
				t = e7;
				break;
			case 8:
				t = e8;
				break;
			case 9:
				t = e9;
				break;
			case 10:
				t = e10;
				break;
			case 11:
				t = e11;
				break;
			case 12:
				t = e12;
				break;
			case 13:
				t = e13;
				break;
			case 14:
				t = e14;
				break;
			case 15:
				t = e15;
				break;
			}// `switch`
			long subCapacity = capacity >> ds;
			t = replace(t, subCapacity, i - k * subCapacity, e);
		}

		switch ((int) k) {
		case 0:
			e0 = t;
			break;
		case 1:
			e1 = t;
			break;
		case 2:
			e2 = t;
			break;
		case 3:
			e3 = t;
			break;
		case 4:
			e4 = t;
			break;
		case 5:
			e5 = t;
			break;
		case 6:
			e6 = t;
			break;
		case 7:
			e7 = t;
			break;
		case 8:
			e8 = t;
			break;
		case 9:
			e9 = t;
			break;
		case 10:
			e10 = t;
			break;
		case 11:
			e11 = t;
			break;
		case 12:
			e12 = t;
			break;
		case 13:
			e13 = t;
			break;
		case 14:
			e14 = t;
			break;
		case 15:
			e15 = t;
			break;
		}// `switch`
		t0 = e0;
		t1 = e1;
		t2 = e2;
		t3 = e3;
		t4 = e4;
		t5 = e5;
		t6 = e6;
		t7 = e7;
		t8 = e8;
		t9 = e9;
		t10 = e10;
		t11 = e11;
		t12 = e12;
		t13 = e13;
		t14 = e14;
		t15 = e15;
	}

	@SuppressWarnings("rawtypes")
	static private Object replace(Object other, long capacity, long i, Object e) {

		if (capacity == 1) {
			return e;
		}

		Object e0 = null, e1 = null, e2 = null, e3 = null, e4 = null, e5 = null, e6 = null, e7 = null, e8 = null,
				e9 = null, e10 = null, e11 = null, e12 = null, e13 = null, e14 = null, e15 = null;

		long k = (i * arity) / capacity;
		long subCapacity = capacity >> ds;
		Object t = e;
		if (other == null) {
			t = null;
			if (e == null) {
				return null;
			}

		} else {
			ImmutableArrayBase tree = (ImmutableArrayBase) other;
			e0 = tree.t0;
			e1 = tree.t1;
			e2 = tree.t2;
			e3 = tree.t3;
			e4 = tree.t4;
			e5 = tree.t5;
			e6 = tree.t6;
			e7 = tree.t7;
			e8 = tree.t8;
			e9 = tree.t9;
			e10 = tree.t10;
			e11 = tree.t11;
			e12 = tree.t12;
			e13 = tree.t13;
			e14 = tree.t14;
			e15 = tree.t15;
			switch ((int) k) {
			case 0:
				t = tree.t0;
				break;
			case 1:
				t = tree.t1;
				break;
			case 2:
				t = tree.t2;
				break;
			case 3:
				t = tree.t3;
				break;
			case 4:
				t = tree.t4;
				break;
			case 5:
				t = tree.t5;
				break;
			case 6:
				t = tree.t6;
				break;
			case 7:
				t = tree.t7;
				break;
			case 8:
				t = tree.t8;
				break;
			case 9:
				t = tree.t9;
				break;
			case 10:
				t = tree.t10;
				break;
			case 11:
				t = tree.t11;
				break;
			case 12:
				t = tree.t12;
				break;
			case 13:
				t = tree.t13;
				break;
			case 14:
				t = tree.t14;
				break;
			case 15:
				t = tree.t15;
				break;
			}// `switch`

		}
		t = replace(t, subCapacity, i - k * subCapacity, e);

		switch ((int) k) {

		case 0:
			e0 = t;
			break;
		case 1:
			e1 = t;
			break;
		case 2:
			e2 = t;
			break;
		case 3:
			e3 = t;
			break;
		case 4:
			e4 = t;
			break;
		case 5:
			e5 = t;
			break;
		case 6:
			e6 = t;
			break;
		case 7:
			e7 = t;
			break;
		case 8:
			e8 = t;
			break;
		case 9:
			e9 = t;
			break;
		case 10:
			e10 = t;
			break;
		case 11:
			e11 = t;
			break;
		case 12:
			e12 = t;
			break;
		case 13:
			e13 = t;
			break;
		case 14:
			e14 = t;
			break;
		case 15:
			e15 = t;
			break;
		}// `switch`

		if ((e == null) && (e0 == null) && (e1 == null) && (e2 == null) && (e3 == null) && (e4 == null) && (e5 == null)
				&& (e6 == null) && (e7 == null) && (e8 == null) && (e9 == null) && (e10 == null) && (e11 == null)
				&& (e12 == null) && (e13 == null) && (e14 == null) && (e15 == null)) {
			return null;
		}
		return new ImmutableArrayBase(capacity, e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15);
	}

	public String debug() {
		StringBuffer str = new StringBuffer();
		str.append('[');
		str.append(size);
		long s = arity;
		int h = 0;
		while (s <= size) {
			s = s * arity;
			h = h + 1;
		}
		str.append(" t0:");
		str.append(subtreeToDbgStr(t0));
		str.append(" t1:");
		str.append(subtreeToDbgStr(t1));
		str.append(" t2:");
		str.append(subtreeToDbgStr(t2));
		str.append(" t3:");
		str.append(subtreeToDbgStr(t3));

		if (arity > 4) {
			str.append(" t4:");
			str.append(subtreeToDbgStr(t4));
			str.append(" t5:");
			str.append(subtreeToDbgStr(t5));
			str.append(" t6:");
			str.append(subtreeToDbgStr(t6));
			str.append(" t7:");
			str.append(subtreeToDbgStr(t7));
		}

		if (arity > 8) {
			str.append(" t8:");
			str.append(subtreeToDbgStr(t8));
			str.append(" t9:");
			str.append(subtreeToDbgStr(t9));
			str.append(" t10:");
			str.append(subtreeToDbgStr(t10));
			str.append(" t11:");
			str.append(subtreeToDbgStr(t11));
			str.append(" t12:");
			str.append(subtreeToDbgStr(t12));
			str.append(" t13:");
			str.append(subtreeToDbgStr(t13));
			str.append(" t14:");
			str.append(subtreeToDbgStr(t14));
			str.append(" t15:");
			str.append(subtreeToDbgStr(t15));
		}

		str.append(']');
		return str.toString();
	}

	@SuppressWarnings("rawtypes")
	private String subtreeToDbgStr(Object t) {
		if (t == null) {
			return null;
		}

		if (!(t instanceof ImmutableArrayBase)) {
			return ("!" + t);
		} else {
			return ((ImmutableArrayBase) t).debug();
		}
	}

	protected Object[] asMutableArray() {
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("tuple too large: Cannot cast size() to int");
		Object[] arr = new Object[(int) size];
		for (int i = 0; i < size; i++) {
			arr[i] = this.get(i);
		}
		return arr;
	}

	@SuppressWarnings("unchecked")
	E firstElement() {
		ImmutableArrayBase<E> tree = this;
		long capacity = arity;
		while (capacity < tree.size) {
			capacity = capacity << ds;
		}

		while (tree != null) {

			Object t = null;
			if (tree.t0 != null)
				t = tree.t0;
			else if (tree.t1 != null)
				t = tree.t1;
			else if (tree.t2 != null)
				t = tree.t2;
			else if (tree.t3 != null)
				t = tree.t3;
			else if (tree.t4 != null)
				t = tree.t4;
			else if (tree.t5 != null)
				t = tree.t5;
			else if (tree.t6 != null)
				t = tree.t6;
			else if (tree.t7 != null)
				t = tree.t7;
			else if (tree.t8 != null)
				t = tree.t8;
			else if (tree.t9 != null)
				t = tree.t9;
			else if (tree.t10 != null)
				t = tree.t10;
			else if (tree.t11 != null)
				t = tree.t11;
			else if (tree.t12 != null)
				t = tree.t12;
			else if (tree.t13 != null)
				t = tree.t13;
			else if (tree.t14 != null)
				t = tree.t14;
			else if (tree.t15 != null)
				t = tree.t15;

			if ((capacity == arity) || (t == null)) {
				return (E) t;
			} else {
				tree = (ImmutableArrayBase<E>) t;
				capacity = capacity >> ds;
			} // `else`
		} // `while`
		return null;
	}

	final protected ForwardIterator<E> notNullIterator() {
		return new NotNullIterator<E>(this);
	}

	/**
	 * 
	 * TODO: Make this faster for sparse arrays
	 * 
	 */
	public static class NotNullIterator<E> implements ForwardIterator<E> {

		private final ExtendibleArray<Object> stack = new ExtendibleArray<Object>();
		private long nodeDepth;

		public NotNullIterator(ImmutableArrayBase<E> array) {
			long capacity = arity;
			nodeDepth = 0;
			while (capacity < array.size) {
				capacity = capacity << ds;
				nodeDepth = nodeDepth + 1;
			}
			advanceToFirstNotNullElement(array);
		}

		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@SuppressWarnings("unchecked")
		public E next() {
			if (stack.isEmpty())
				throw new NoSuchElementException("no next element");

			E result = (E) stack.removeFirst();
			if (stack.size() == nodeDepth)
				advanceToNextNotNullElement();

			return result;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void advanceToFirstNotNullElement(ImmutableArrayBase<E> tree) {
			for (int i = (int) stack.size(); i < nodeDepth; i++) {
				Object t = null;
				if (tree.t0 != null) {
					t = tree.t0;
					stack.addFirst(new ImmutableArrayBase(0, null, tree.t1, tree.t2, tree.t3, tree.t4, tree.t5, tree.t6,
							tree.t7, tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t1 != null) {
					t = tree.t1;
					stack.addFirst(new ImmutableArrayBase(0, null, null, tree.t2, tree.t3, tree.t4, tree.t5, tree.t6,
							tree.t7, tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t2 != null) {
					t = tree.t2;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, tree.t3, tree.t4, tree.t5, tree.t6,
							tree.t7, tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t3 != null) {
					t = tree.t3;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, tree.t4, tree.t5, tree.t6, tree.t7,
							tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t4 != null) {
					t = tree.t4;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, tree.t5, tree.t6, tree.t7,
							tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t5 != null) {
					t = tree.t5;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, tree.t6, tree.t7,
							tree.t8, tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t6 != null) {
					t = tree.t6;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, tree.t7, tree.t8,
							tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t7 != null) {
					t = tree.t7;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, tree.t8,
							tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t8 != null) {
					t = tree.t8;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null,
							tree.t9, tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t9 != null) {
					t = tree.t9;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							tree.t10, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t10 != null) {
					t = tree.t10;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, tree.t11, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t11 != null) {
					t = tree.t11;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, null, tree.t12, tree.t13, tree.t14, tree.t15));
				} else if (tree.t12 != null) {
					t = tree.t12;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, null, null, tree.t13, tree.t14, tree.t15));
				} else if (tree.t13 != null) {
					t = tree.t13;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, tree.t14, tree.t15));
				} else if (tree.t14 != null) {
					t = tree.t14;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, tree.t15));
				} else if (tree.t15 != null) {
					t = tree.t15;
					stack.addFirst(new ImmutableArrayBase(0, null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null));
				}

				if (t == null) {
					/*
					 * The tree is empty:
					 */
					stack.clear();
					return;
				}
				tree = (ImmutableArrayBase<E>) t;
			} // `while`
			pushNotNullElements(tree);

		}

		private void pushNotNullElements(ImmutableArrayBase<E> tree) {
			if (tree.t15 != null)
				stack.addFirst(tree.t15);
			if (tree.t14 != null)
				stack.addFirst(tree.t14);
			if (tree.t13 != null)
				stack.addFirst(tree.t13);
			if (tree.t12 != null)
				stack.addFirst(tree.t12);
			if (tree.t11 != null)
				stack.addFirst(tree.t11);
			if (tree.t10 != null)
				stack.addFirst(tree.t10);
			if (tree.t9 != null)
				stack.addFirst(tree.t9);
			if (tree.t8 != null)
				stack.addFirst(tree.t8);
			if (tree.t7 != null)
				stack.addFirst(tree.t7);
			if (tree.t6 != null)
				stack.addFirst(tree.t6);
			if (tree.t5 != null)
				stack.addFirst(tree.t5);
			if (tree.t4 != null)
				stack.addFirst(tree.t4);
			if (tree.t3 != null)
				stack.addFirst(tree.t3);
			if (tree.t2 != null)
				stack.addFirst(tree.t2);
			if (tree.t1 != null)
				stack.addFirst(tree.t1);
			if (tree.t0 != null)
				stack.addFirst(tree.t0);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void advanceToNextNotNullElement() {
			while (stack.size() <= nodeDepth) {
				if (stack.isEmpty())
					return;

				ImmutableArrayBase node = (ImmutableArrayBase<E>) stack.removeFirst();
				if (node.t1 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, node.t1, node.t2, node.t3, node.t4,
							node.t5, node.t6, node.t7, node.t8, node.t9, node.t10, node.t11, node.t12, node.t13,
							node.t14, node.t15));
				else if (node.t2 != null)
					advanceToFirstNotNullElement(
							new ImmutableArrayBase(0, null, null, node.t2, node.t3, node.t4, node.t5, node.t6, node.t7,
									node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t3 != null)
					advanceToFirstNotNullElement(
							new ImmutableArrayBase(0, null, null, null, node.t3, node.t4, node.t5, node.t6, node.t7,
									node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t4 != null)
					advanceToFirstNotNullElement(
							new ImmutableArrayBase(0, null, null, null, null, node.t4, node.t5, node.t6, node.t7,
									node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t5 != null)
					advanceToFirstNotNullElement(
							new ImmutableArrayBase(0, null, null, null, null, null, node.t5, node.t6, node.t7, node.t8,
									node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t6 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, node.t6,
							node.t7, node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t7 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							node.t7, node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t8 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, node.t8, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t9 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, node.t9, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t10 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, node.t10, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t11 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, null, node.t11, node.t12, node.t13, node.t14, node.t15));
				else if (node.t12 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, null, null, node.t12, node.t13, node.t14, node.t15));
				else if (node.t13 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, null, null, null, node.t13, node.t14, node.t15));
				else if (node.t14 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, node.t14, node.t15));
				else if (node.t15 != null)
					advanceToFirstNotNullElement(new ImmutableArrayBase(0, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, node.t15));
			}
		}
	}

	/**
	 * This method takes another array and returns an iterator over all places which
	 * have non-identical objects. It is assumed that both arrays have the same
	 * size.
	 * 
	 * The method is useful as an implementation base for history-aware data
	 * structures.
	 */
	protected ForwardIterator<Long> nonIdenticalPlaces(ImmutableArrayBase<?> other) {
		Assertions.assertEquals("Both arrays must have the same size", this.size, other.size);
		return null; // TODO
	}

}// `class`
