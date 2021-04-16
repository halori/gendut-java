package org.gendut.seq;

import java.util.Comparator;

import org.gendut.collection.AbstractList;
import org.gendut.collection.Array;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;

public final class MergedSortedSeqs<E> extends AbstractList<E> implements Seq<E> {

    public static <E> Seq<E> createRoundRobin(Array<Seq<E>> seqs, Comparator<? super E> cmp) {
        return new MergedSortedSeqs<E>(seqs, cmp, true);
    }
    
	public static <E> Seq<E> create(Array<Seq<E>> seqs, Comparator<? super E> cmp) {
		return new MergedSortedSeqs<E>(seqs, cmp, false);
	}
	
	private final E min;
	private final ExtendibleArray<Seq<E>> rest;
	private final Comparator<? super E> cmp;
	private final boolean isEmpty;
	private final boolean roundRobinMode;

	private MergedSortedSeqs(Array<Seq<E>> seqs, Comparator<? super E> cmp, boolean roundRobinMode) {

		this.cmp = cmp;
		this.roundRobinMode = roundRobinMode;
		
		int pivot = findMin(seqs, cmp);

		if (pivot == -1) {
			isEmpty = true;
			this.rest = null;
			this.min = null;
		}
		else {
			isEmpty = false;
			rest = new ExtendibleArray<Seq<E>>();
			E minElement = null;
			
            for (int cnt = 0; cnt < seqs.size(); cnt++) {
                int i = roundRobinMode ? (pivot+1+cnt) % (int) seqs.size() : cnt;
				Seq<E> seq = seqs.get(i);
				if (i == pivot) {
					minElement  = seq.first();
					seq = seq.rest();
				}
				if (!seq.isEmpty()) {
					rest.add(seq);
				}
			}
			this.min = minElement;
		}
	}

	private static <E> int findMin(Array<Seq<E>> seqs, Comparator<? super E> cmp) {
		E minVal = null;
		int minPos = -1;
		for (int i = 0; i < seqs.size(); i++) {
			Seq<E> seq = seqs.get(i);
			if (!seq.isEmpty()) {
				E e = seq.first();
				if (minPos == -1 || cmp.compare(e, minVal) < 0) {
					minPos = i;
					minVal = e;
				}
			}
		}
		return minPos;
	}

	@Override
	public E first() {
		return min;
	}

	@Override
	public Seq<E> rest() {
		return new MergedSortedSeqs<E>(rest.asConstant(), cmp, roundRobinMode);
	}

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}
	
	@Override
	public ForwardIterator<E> iterator() {
		return new IteratorFromSeq<E>(this);
	}
}
