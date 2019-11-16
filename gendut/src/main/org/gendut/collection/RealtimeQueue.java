package org.gendut.collection;

import org.gendut.arithmetic.Int;
import org.gendut.func.Pair;
import org.gendut.func.Function;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;
import org.gendut.seq.Seq;

//!Immutable Deque
/*<literate>*/
/**
 * This class is an implementation of an immutable queue as described the book
 * "Purely Functional Data Structures" by Chris Okasaki. The queue is realtime,
 * in the sense that each operation takes O(1) time.
 */
public final class RealtimeQueue<E> implements ImmutableCollection<E>, Array<E> {

	private final LazySeq<E> front;

	private final Stack<E> rear;

	private final LazySeq<E> schedule;

	private final long length;

	private RealtimeQueue() {
		front = schedule = LazySeq.empty();
		rear = Stack.create();
		length = 0;
	}

	private RealtimeQueue(LazySeq<E> front, Stack<E> rear, LazySeq<E> schedule, long length) {
		super();
		this.front = front;
		this.rear = rear;
		this.schedule = schedule;
		this.length = length;
	}

	@SuppressWarnings("rawtypes")
	private static RealtimeQueue emptyInstance = new RealtimeQueue();

	@SuppressWarnings("unchecked")
	static public <E> RealtimeQueue<E> create() {
		return emptyInstance;
	}

	public E first() {
		if (length == 0)
			throw new IllegalArgumentException("Queue is empty");
		return front.first();
	}

	public RealtimeQueue<E> rest() {
		if (length == 0)
			throw new IllegalArgumentException("Queue is empty");
		else
			return exec(front.rest(), rear, schedule, length - 1);
	}

	/**
	 * Add a new element at the end of the queue.
	 */
	public RealtimeQueue<E> append(E e) {
		return exec(front, rear.push(e), schedule, length + 1);
	}

	/**
	 * Scheduled rotation of the list.
	 */
	static private <E> RealtimeQueue<E> exec(LazySeq<E> front, Stack<E> rear, LazySeq<E> schedule, long length) {
		if (!schedule.isEmpty())
			return new RealtimeQueue<E>(front, rear, schedule.rest(), length);
		else {
			LazySeq<E> emptyList = LazySeq.empty();
			front = rotate(front, rear, emptyList, 0);
			Stack<E> emptyStack = Stack.create();
			return new RealtimeQueue<E>(front, emptyStack, front, length);
		}
	}

	/**
	 * Rotate the next scheduled part of the list. Rotation calls itself
	 * recursively. In order to perform rotation in O(1) time (worst case), a
	 * lazy list node is constructed after a maximum number of recursions.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static private <E> LazySeq<E> rotate(LazySeq<E> front, Stack<E> rear, LazySeq<E> schedule, int recDepth) {
		if (front.isEmpty())
			return new LazySeq<E>(rear.first(), schedule);
		else {
			if (recDepth < 3) {
				return new LazySeq(front.first(),
						rotate(front.rest(), rear.rest(), new LazySeq<E>(rear.first(), schedule), recDepth + 1));
			} else {
				Pair<LazySeq<E>, Pair<Stack<E>, LazySeq<E>>> arg = Pair.create(front.rest(),
						Pair.create(rear.rest(), new LazySeq<E>(rear.first(), schedule)));
				return new LazySeq(front.first(), rotateRec, arg);
			}
		}
	}

	/**
	 * Closure for the recursive call that is used in the lazy nodes:
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Function<Pair<LazySeq, Pair<Stack, LazySeq>>, LazySeq> rotateRec = new Function<Pair<LazySeq, Pair<Stack, LazySeq>>, LazySeq>() {
		public LazySeq get(Pair<LazySeq, Pair<Stack, LazySeq>> arg) {
			return rotate(arg.first(), (Stack) arg.second().first(), arg.second().second(), 0);
		}
	};

	/*
	 * Implementation of standard methods:
	 */

	public boolean contains(E e) {
		return Collections.containsViaIterator(this, e);
	}

	public ForwardIterator<E> iterator() {
		return new IteratorFromSeq<E>(this);
	}

	public Seq<E> seq() {
		return this;
	}

	public long size() {
		return length;
	}

	public Int elementCount() {
		return Int.create(size());
	}

	public E find(E e) {
		return Collections.findViaIterator(this, e);
	}

	@Override
	public boolean equals(Object obj) {
		return Collections.equalsForList(this, obj);
	}

	@Override
	public int hashCode() {
		return Collections.hashCodeForLists(this);
	}

	@Override
	public String toString() {
		return Collections.toStringIterationOrder(this);
	}

	public E get(long i) {
		Seq<E> s = this;
		while (i > 0) {
			s = s.rest();
			i = i - 1;
		}
		return s.first();
	}

	public boolean isEmpty() {
		return (length == 0);
	}

	@SuppressWarnings("unchecked")
	public RealtimeQueue<E> clear() {
		return emptyInstance;
	}

	@Override
	public Stream<E> stream() {
		return Collections.stream(this);
	}
}// `class`
