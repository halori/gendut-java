package org.gendut.collection;

import org.gendut.algorithm.Comparator;
import org.gendut.func.Pair;
import org.gendut.seq.Seq;

/**
 * Standard implementation for unattributed intervals. Since interval records don't
 * carry additional information beneath its boundaries and the underlying order,
 * some meaningful interval operations like intersection and hull are added.
 */
public final class Interval<T> implements IntervalRecord<T, Interval<T>> {
	final T start;

	final T end;

	final Comparator<? super T> baseComparator;

	public Interval(T start, T end, Comparator<? super T> baseComparator) {
		if (baseComparator.compare(start, end) >= 0)
			throw new IllegalArgumentException(
					"interval boundaries in wrong order.");
		this.start = start;
		this.end = end;
		this.baseComparator = baseComparator;
	}

	public T start() {
		return start;
	}

	public T end() {
		return end;
	}

	public Comparator<? super T> baseComparator() {
		return baseComparator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		if (baseComparator == null) {
			if (other.baseComparator != null)
				return false;
		} else if (!baseComparator.equals(other.baseComparator))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + start + "," + end + ")";
	}

	public int compareEndTo(T p) {
		return baseComparator.compare(end, p);
	}

	public int compare(Interval<T> B) {
		int c = baseComparator.compare(start, B.start());
		if (c != 0)
			return c;
		else
			return baseComparator.compare(end, B.end());
	}

	/**
	 * Returns the gap between this and the other interval.
	 */
	public Interval<T> gap(Interval<T> other) {
		if (this.intersects(other))
			return null;
		if (baseComparator.compare(start, other.start()) <= 0)
			return new Interval<T>(end, other.start(), baseComparator);
		else
			return new Interval<T>(other.end(), start, baseComparator);
	}

	/**
	 * Returns the minimal interval that contains this and the other interval.
	 */
	public Interval<T> intersect(Interval<T> other) {
		T a = max(this.start, other.start());
		T b = min(this.start, other.start());
		if (a == start && b == end)
			return this;
		if (baseComparator.compare(a, b) < 0)
			return new Interval<T>(a, b, baseComparator);
		else
			return null;
	}

	public boolean intersects(Interval<T> B) {
		return intersects(B.start(), B.end());
	}

	public boolean intersects(T start, T end) {
		if (baseComparator.compare(this.end, start) <= 0)
			return false;
		if (baseComparator.compare(end, this.start) <= 0)
			return false;
		return true;
	}

	public int compareEnds(Interval<T> B) {
		return baseComparator.compare(end, B.end());
	}

	/**
	 * Returns the minimal interval that contains this and the other interval.
	 */
	public Interval<T> minimumHull(Interval<T> other) {
		T a = min(this.start, other.start());
		T b = max(this.start, other.start());
		if (a == start && b == end)
			return this;
		else
			return new Interval<T>(a, b, baseComparator);
	}

	/**
	 * Returns a set of intervals which covers the intervals resulting from
	 * subtracting interval B from this interval.
	 */
	public Seq<Interval<T>> minus(Interval<T> other) {
		Stack<Interval<T>> list = Stack.create();
		if (baseComparator.compare(start, other.start()) < 0)
			list = list.push(new Interval<T>(start, other.start(),
					baseComparator));
		if (baseComparator.compare(other.end(), end) < 0)
			list = list.push(new Interval<T>(other.end(), end(),
					baseComparator));
		return list;
	}

	private T min(T a, T b) {
		return baseComparator.compare(a, b) <= 0 ? a : b;
	}

	private T max(T a, T b) {
		return baseComparator.compare(a, b) >= 0 ? a : b;
	}

	public boolean contains(T point) {
		return baseComparator.compare(start, point) <= 0
				&& baseComparator.compare(point, end) < 0;
	}

	public boolean contains(Interval<T> other) {
		return baseComparator.compare(start, other.start()) <= 0
				&& baseComparator.compare(other.end(), end) <= 0;
	}

	/**
	 * splits an interval into two pieces, where the first piece is the old
	 * interval before the given point, and the second piece is the interval up
	 * from (inclusive) the given point. If the point is not inside of the given
	 * interval, or if one of the two pieces isempty, an exception is raised.
	 */
	public Pair<Interval<T>, Interval<T>> split(T point) {
		if (!contains(point))
			throw new IllegalArgumentException("Point " + point
					+ " is not inside interval " + this);
		Interval<T> firstPiece = new Interval<T>(start, point,
				baseComparator);
		Interval<T> secondPiece = new Interval<T>(point, end,
				baseComparator);
		return Pair.create(firstPiece, secondPiece);
	}

}
