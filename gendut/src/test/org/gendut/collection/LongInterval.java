package org.gendut.collection;

import java.util.Comparator;

import org.gendut.seq.Seq;

public final class LongInterval implements IntervalRecord<Long, LongInterval> {
	private final long start, end;

	public LongInterval(long start, long end) {
		if (start > end)
			throw new IllegalArgumentException(
					"interval boundaries in wrong order.");
		this.start = start;
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (end ^ (end >>> 32));
		result = prime * result + (int) (start ^ (start >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "[" + start + "," + end + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongInterval other = (LongInterval) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	final static Comparator<Long> cmp = new Comparator<Long>() {
		public int compare(Long a, Long b) {
			if (a < b)
				return -1;
			else if (a > b)
				return 1;
			else
				return 0;
		}
	};

	public Comparator<Long> baseComparator() {
		return cmp;
	}

	public int compare(LongInterval B) {
		LongInterval b = (LongInterval) B;
		if (start < b.start)
			return -1;
		else if (start > b.start)
			return 1;
		else if (end < b.end)
			return -1;
		else if (end > b.end)
			return 1;
		else
			return 0;
	}

	public int compareEndTo(Long p) {
		if (end < p)
			return -1;
		else if (end > p)
			return 1;
		else
			return 0;
	}

	public int compareEnds(LongInterval B) {
		LongInterval b = (LongInterval) B;
		if (end < b.end)
			return -1;
		else if (end > b.end)
			return 1;
		else
			return 0;
	}

	public Long end() {
		return end;
	}

	public LongInterval gap(LongInterval other) {
		if (this.intersects(other))
			return null;
		if (start <= other.start())
			return new LongInterval(end, other.start());
		else
			return new LongInterval(other.end(), start);
	}

	public LongInterval intersect(LongInterval other) {
		long a = max(this.start, other.start());
		long b = min(this.start, other.start());
		if (a == start && b == end)
			return this;
		if (a < b)
			return new LongInterval(a, b);
		else
			return null;
	}

	public boolean intersects(Long start, Long end) {
		if (this.end <= start)
			return false;
		if (end <= this.start)
			return false;
		return true;
	}

	public boolean intersects(LongInterval B) {
		LongInterval b = (LongInterval) B;
		if (end <= b.start)
			return false;
		if (b.end <= start)
			return false;
		return true;
	}

	public LongInterval minimumHull(LongInterval other) {
		long a = min(this.start, other.start());
		long b = max(this.start, other.start());
		if (a == start && b == end)
			return this;
		else
			return new LongInterval(a, b);
	}

	public Seq<LongInterval> minus(LongInterval other) {
		Stack<LongInterval> list = Stack.create();
		if (start < other.start())
			list = list.push(new LongInterval(start, other.start()));
		if (other.end() < end)
			list = list.push(new LongInterval(other.end(), end()));
		return list;
	}

	public Long start() {
		return start;
	}

	static private long min(long a, long b) {
		return a <= b ? a : b;
	}

	static private long max(long a, long b) {
		return a >= b ? a : b;
	}

	public boolean contains(Long point) {
		return start <= point && point < end;
	}

	public boolean contains(LongInterval other) {
		return start <= other.start() && other.end() <= end;
	}

}
