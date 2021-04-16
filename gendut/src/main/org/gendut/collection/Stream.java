package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.errors.Assertions;
import org.gendut.func.Function;
import org.gendut.func.Functions;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.Seqs;
import org.gendut.seq.TransformedSeq;

public final class Stream<E> extends AbstractList<E> implements Seq<E> {
	private final Seq<E> seq;

	/**
	 * constructor is package private to allow optimizations. Use
	 * Collections.stream(...) instead.
	 */
	Stream(Seq<E> seq) {
		this.seq = seq;
	}

	@Override
	public ForwardIterator<E> iterator() {
		return seq.iterator();
	}

	@Override
	public E first() {
		return seq.first();
	}

	@Override
	public Stream<E> rest() {
		return new Stream<E>(seq.rest());
	}

	@Override
	public boolean isEmpty() {
		return seq.isEmpty();
	}

	public Seq<E> asSeq() {
		return seq;
	}

	public <F> Stream<F> map(Function<E, F> map) {
		return new Stream<F>(TransformedSeq.create(this, map));
	}

	public Stream<E> filter(Function<E, Boolean> filter) {
		Function<E, E> id = Functions.id();
		return new Stream<E>(TransformedSeq.create(seq, filter, id));
	}

	public Stream<E> sorted(Comparator<? super E> cmp) {
		ConstantArray<E> arr = ConstantArray.fromSequence(seq);
		return new Stream<E>(Collections.sort(arr, cmp));
	}

	public Stream<E> limit(long maxSize) {
		return new Stream<E>(Collections.limit(seq, maxSize));
	}

	public Stream<E> skip(long maxSize) {
		return new Stream<E>(Collections.skip(seq, maxSize));
	}

	public E reduce(BinaryFunction<E, E, E> op, E e) {
		return Collections.reduce(seq, op, e);
	}

	public Int count() {
		return Collections.count(seq);
	}

	public long size() {
		return Collections.size(seq);
	}

	public long indexOf(E e) {
		return Collections.indexOf(seq, e);
	};

	public Array<E> toArray() {
		long sz = size();
		if (sz < Integer.MAX_VALUE)
			return ConstantArray.fromSequence(seq);
		else {
			ImmutableArray<E> arr = ImmutableArray.create(sz);
			Seq<E> sq = seq;
			for (int i = 0; i < sz; i++) {
				arr.set(i, sq.first());
				sq = sq.rest();
			}
			Assertions.assertion("Sequence should be empty.", sq.isEmpty());
			return arr;
		}
	}
	
	public Stream<E> concatMany(Seq<Seq<E>> seqs) {
		return new Stream<E>(Collections.concat(Seqs.appendBefore(seq, seqs)));
	}
	
	public Stream<E> concat(Seq<E> otherSeq) {
		Stack<Seq<E>> seqs = Stack.create();
		seqs = seqs.push(otherSeq);
		seqs = seqs.push(this.seq);
		return new Stream<E>(Collections.concat(seqs));
	}
}
