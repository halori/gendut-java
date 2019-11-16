package org.gendut.seq;

public class Finder extends FunctionBase<Seq<Long>, Long> {

	long k;

	public Finder(long k) {
		super();
		this.k = k;
	}

	@Override
	public Long get(Seq<Long> seq) {

		long count = 0;
		while (seq.first() != k) {
			count++;
			seq = seq.rest();
		}
		return count;

	}
}
