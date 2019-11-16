package org.gendut.collection;

import org.gendut.func.Function;
import org.gendut.func.LazyValue;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;
import org.gendut.seq.Seq;

//! Immutable Lazy Sequence 
/*<literate>*/
/**
 * This is a immutable list that allows lazy <i>first</i> and <i>rest</i>
 * operations.<br />
 */
public final class LazySeq<E> extends AbstractList<E> implements Seq<E> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static private LazySeq empty = new LazySeq(null, null);

	@SuppressWarnings("unchecked")
	static public <E> LazySeq<E> empty() {
		return empty;
	}

	static private final class MyLazy<T> extends LazyValue<T> {

		/*
		 * We use a specialization of <i>LazyValue</i> which is only internally
		 * visible. This allows to use the public class <i>LazyValue</i> as a
		 * type parameter.
		 */
		public <R> MyLazy(Function<? super R, ? extends T> f, R arg) {
			super(f, arg);
		}
	};

	/*
	 * <i>first</i> can hold objects or lazy thunks of type E.<br /> <i>rest</i>
	 * can hold lists or lazy thunks of lists of type E.<br />
	 */
	volatile private Object first;
	volatile private Object rest;

	/**
	 * Constructor for "consing" <i>first</i> and <i>rest</i>
	 */
	public LazySeq(E first, LazySeq<? extends E> rest) {
		this.first = first;
		if (rest == null)
			rest = empty();
		this.rest = rest;
	}

	/**
	 * Constructor for "consing" <i>first</i> and a lazy <i>rest</i>. The rest
	 * of the list is described by a function and an argument which is evaluated
	 * on access.
	 */
	public <F> LazySeq(E first,
			Function<? super F, ? extends LazySeq<? extends E>> restFun,
			F restArg) {
		this.first = first;
		this.rest = new MyLazy<LazySeq<? extends E>>(restFun, restArg);
	}

	/**
	 * Constructor for "consing" a lazy <i>first</i> and a <i>rest</i>. The
	 * first element of the list is described by a function and an argument
	 * which is evaluated on access.
	 */
	public <D> LazySeq(Function<? super D, ? extends E> firstFun, D firstArg,
			LazySeq<? extends E> rest) {
		this.first = new MyLazy<E>(firstFun, firstArg);
		this.rest = rest;
	}

	/**
	 * Constructor for "consing" a lazy <i>first</i> and a lazy <i>rest</i>.
	 * Each is described by a function and an argument which is evaluated on
	 * access.
	 */
	public <D, F> LazySeq(Function<? super D, ? extends E> firstFun,
			D firstArg,
			Function<? super F, ? extends LazySeq<? extends E>> restFun,
			F restArg) {
		this.first = new MyLazy<E>(firstFun, firstArg);
		this.rest = new MyLazy<LazySeq<? extends E>>(restFun, restArg);
	}

	/**
	 * returns the <i>i</i> element of the list. The operation needs <i>O(i)</i>
	 * time.
	 */
	public E first(int i) {
		return getValue(this, i);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <E> E getValue(LazySeq<E> node, int i) {
		int index = i;
		while (node != empty) {
			if (i == 0) {
				if (node.first.getClass() == MyLazy.class)
					node.first = ((MyLazy) node.first).value();

				return (E) node.first;
			} else {
				node = node.getRest();
				i = i - 1;
			}
		}// `while`
		throw new IndexOutOfBoundsException("List has less than " + index
				+ " elements.");
	}

	/**
	 * Returns the sublist by removing the first <i>i</i> element. The operation
	 * needs <i>O(i)</i> time.
	 */
	public LazySeq<E> rest(int i) {
		return rest(this, i);
	}

	private static <E> LazySeq<E> rest(LazySeq<E> node, int i) {
		int index = i;
		while (node != empty) {
			if (i == 0)
				return node.getRest();
			else {
				node = node.getRest();
				i = i - 1;
			}
		}// `while`
		throw new IndexOutOfBoundsException("List has less than " + index
				+ " elements.");
	}

	@SuppressWarnings("unchecked")
	private LazySeq<E> getRest() {
		if (rest == empty)
			return empty;
		if (rest.getClass() == MyLazy.class)
			rest = ((MyLazy<LazySeq<E>>) rest).value();
		if (rest == null)
			rest = empty;
		return (LazySeq<E>) rest;
	}

	public E first() {
		return first(0);
	}

	public LazySeq<E> rest() {
		return rest(0);
	}

	public boolean isEmpty() {
		return (this == empty);
	}
	
	@Override
	public ForwardIterator<E> iterator() {
		return new IteratorFromSeq<E>(this);
	}
}
