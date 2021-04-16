package org.gendut.collection;

import java.util.Comparator;

// ! Intervalrecord
/* <literate> */
/**
 * Value objects for attributed intervals. All intervals are treated as if they were left-closed and
 * right-open. In order to change this, one should introduce for each element a from T a value a'
 * such that a' > a and a' < b for all b > a. Then [a',c) corresponds to (a,c), [d,a') corresponds
 * to [d,a], and [a',c') corresponds to (a,c].
 * 
 * Methods that take a second interval as an argument only need to work as expected if both
 * intervals are compatible (same class, same underlying order).
 * 
 * If <code>equals()</code> is overwritten, it is required that whenever two intervals are
 * identified by equality, they must also be identified by the lexicographic order of its
 * boundaries. This requirement ensures that data-structures which use only the lexicogtraphic order
 * can implement set-behavior (which is defined by equality). This holds, for instance, if intervals
 * have attributes and equality extends the lexicographic order by taking the attribute value into
 * account.
 **/
public interface IntervalRecord<T, I extends IntervalRecord<T, I>>
{
    public T start();

    public T end();

    public Comparator<? super T> baseComparator();

    /**
     * Comparator for lexicographic order (or strengthening) of it for intervals from the same
     * class. That means: It is allowed that the order relation of an interval class separates
     * intervals with the same boundaries, which can be useful if the intervals are attributed.
     * However, if two intervals have different boundaries, they must be in the came order as the
     * lexicographic order of the boundaries would imply. Furthermore, two intervals must be
     * identified by <code>equals()</code>, if and only if the result of the comparison must be 0.
     * This ensures that data-structures which use comparison can implement set-behavior (which is
     * defined by equality).
     * 
     * Contrary to all other methods defined in this interface, this method is only defined for
     * intervals of the same class.
     */
    public int compare(I B);

    public boolean contains(T point);

    public boolean contains(I other);

    /**
     * Compares the end o this interval to a given point
     */
    public int compareEndTo(T p);

    /**
     * compares the ends of two intervals.
     */
    public int compareEnds(I B);

    /**
     * Tests if this interval and B have common points
     */
    public boolean intersects(I B);

    /**
     * Tests if this interval and interval described by boundaries have common points
     */
    public boolean intersects(T start, T end);

}// `class`
