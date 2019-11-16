package org.gendut.seq;

import junit.framework.TestCase;

import org.gendut.func.Function;
import org.gendut.iterator.ForwardIterator;

public class TransformedSeqTest extends TestCase {

	static Seq<Long> numbers() {
		return SeqFromIterator.create(new ForwardIterator<Long>() {

			long current = 0;

			@Override
			public Long next() {

				return current++;
			}

			@Override
			public boolean hasNext() {
				return true;
			}
		});
	}

	static final long n = 100; // use larger n (10000000) for larger memory leak

	// JVM (Java 7) causes a memory leak if implementation of "get" is
	// "overwritten" from the best known subclass or interface. This means that
	// a reference to "numbers" will be kept somewhere on the heap.
	public void testFinder1() {

		((FunctionBase<Seq<Long>, Long>) finder()).get(numbers());
	}

	// JVM (Java 7) does not cause a memory leak if implementation of "get" is
	// not "overwritten"
	public void testFinder2() {

		((Finder) finder()).get(numbers());
	}

	static Function<Seq<Long>, Long> finder() {
		return new Finder(n);

	}

}
