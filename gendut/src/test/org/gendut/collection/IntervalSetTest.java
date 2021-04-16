package org.gendut.collection;

import java.util.Comparator;
import java.util.Random;

import junit.framework.TestCase;

import org.gendut.arithmetic.Int;
import org.gendut.arithmetic.Rational;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;


public class IntervalSetTest extends TestCase
{
    Comparator<Rational> cmp = Rational.naturallOrder;

    public void tesInsertIntoTree()
    {
        Object tree = new Interval<Int>(Int.create(0), Int.create(2), cmp);
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(3), Int.create(5), cmp));
        assertEquals("([0,2) 5:[3,5) .)", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(3), Int.create(5), cmp));
        assertEquals("([0,2) 5:[3,5) .)", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(5), Int.create(7), cmp));
        assertEquals("([0,2) 7:[3,5) [5,7))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(2), Int.create(7), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 7:[3,5) [5,7))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(8), Int.create(9), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 9:[3,5) ([5,7) 9:[8,9) .))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(10), Int.create(11), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 11:[3,5) ([5,7) 11:[8,9) [10,11)))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(12), Int.create(13), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 13:[3,5) ([5,7) 13:[8,9) ([10,11) 13:[12,13) .)))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(12), Int.create(14), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 14:[3,5) ([5,7) 14:[8,9) ([10,11) 14:[12,13) [12,14))))", tree.toString());
        tree = IntervalSet.insert(tree, new Interval<Int>(Int.create(13), Int.create(14), cmp));
        assertEquals("(([0,2) 7:[2,7) .) 14:[3,5) (([5,7) 11:[8,9) [10,11)) 14:[12,13) ([12,14) 14:[13,14) .)))", tree
                        .toString());
        assertEquals(2, IntervalSet.height(IntervalSet.left(tree)));
        assertEquals(3, IntervalSet.height(IntervalSet.right(tree)));
    }

    int N = 1200;

    private long randomSeed = 12345;

    public void testSortedInsertIntoTree()
    {
        for (int k = 1; k < N; k = 2 * k)
        {
            Object tree = IntervalSet.emptyTree;
            for (int i = 0; i < k; i++)
            {
                Interval<Int> s = new Interval<Int>(Int.create(4 * i), Int.create(4 * i + 1), cmp);
                tree = IntervalSet.insert(tree, s);
            }
            assertEquals(k, IntervalSet.count(tree));
            for (int i = 0; i < 2 * k; i++)
            {
                Interval<Int> s = new Interval<Int>(Int.create(-4 * i - 1), Int.create(-4 * i), cmp);
                tree = IntervalSet.insert(tree, s);
            }
            assertEquals(3 * k, IntervalSet.count(tree));
        }
    }

    public void testSortedLongIntervalsInsertIntoTree()
    {
        for (int k = 1; k < N; k = 2 * k)
        {
            Object tree = IntervalSet.emptyTree;
            for (int i = 0; i < k; i++)
            {
                LongInterval s = new LongInterval(4 * i, 4 * i + 1);
                tree = IntervalSet.insert(tree, s);
            }
            assertEquals(k, IntervalSet.count(tree));
            for (int i = 0; i < 2 * k; i++)
            {
                LongInterval s = new LongInterval(-4 * i - 1, -4 * i);
                tree = IntervalSet.insert(tree, s);
            }
            assertEquals(3 * k, IntervalSet.count(tree));
        }
    }

    public void testCreateFromCollection()
    {
        for (int k = 1; k < N; k = 2 * k)
        {
            ExtendibleArray<LongInterval> intervals = new ExtendibleArray<LongInterval>();
            for (int i = 0; i < k; i++)
            {
                LongInterval s = new LongInterval(4 * i, 4 * i + 1);
                intervals.add(s);
            }
            for (int i = 0; i < 2 * k; i++)
            {
                LongInterval s = new LongInterval(-4 * i - 1, -4 * i);
                intervals.add(s);
            }
            IntervalSet<Long, LongInterval> tree = IntervalSet.fromCollection(intervals.asConstant());
            assertEquals(3 * k, tree.size());
        }
    }

    public void testSortedInsertAndRemove()
    {
        IntervalSet<Long, LongInterval> intervals = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            intervals = intervals.add(new LongInterval(2 * i, 2 * i + 1));
            assertEquals(i + 1, intervals.size());
        }
        for (int i = N - 1; i > 0; i--)
        {
            intervals = intervals.remove(new LongInterval(2 * i, 2 * i + 1));
            assertEquals(i, intervals.size());
        }
        for (int i = 0; i < N; i++)
        {
            intervals = intervals.add(new LongInterval(-2 * i, -2 * i + 1));
            assertEquals(i + 1, intervals.size());
        }
        for (int i = N - 1; i > 0; i--)
        {
            intervals = intervals.remove(new LongInterval(-2 * i, -2 * i + 1));
            assertEquals(i, intervals.size());
        }
    }

    public void testIteratorAndEquals()
    {
        Random rnd = new Random(randomSeed);
        ExtendibleArray<LongInterval> unsorted = new ExtendibleArray<LongInterval>();
        IntervalSet<Long, LongInterval> set = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            long a = 0;
            long b = 0;
            while (b <= a)
            {
                a = rnd.nextInt(100);
                b = rnd.nextInt(100);
            }
            LongInterval interval = new LongInterval(a, b);
            unsorted.add(interval);
            set = set.add(interval);
        }
        Array<LongInterval> sorted = Collections.sortUnique(unsorted.asConstant(), IntervalSet.intervalComperator);
        assertEquals(sorted.size(), set.size());
        ForwardIterator<LongInterval> sortedIterator = sorted.iterator();
        ForwardIterator<LongInterval> setIterator = set.iterator();
        for (long i = sorted.size(); i > 0; i--)
        {
            assertTrue(setIterator.hasNext());
            LongInterval a = setIterator.next();
            LongInterval b = sortedIterator.next();
            assertTrue(a.compare(b) == 0);
        }
        assertFalse(setIterator.hasNext());
        assertEquals(IntervalSet.fromCollection(unsorted.asConstant()), set);
    }

    public void testFind()
    {
        Random rnd = new Random();
        rnd.setSeed(randomSeed);
        ImmutableHashSet<LongInterval> controlSet = ImmutableHashSet.create();
        IntervalSet<Long, LongInterval> set = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            long a = 0;
            long b = 0;
            while (b <= a)
            {
                a = rnd.nextInt(100);
                b = rnd.nextInt(100);
            }
            LongInterval interval = new LongInterval(a, b);
            assertEquals(controlSet.contains(interval), set.contains(interval));
            assertSame(controlSet.find(interval), set.find(interval));
            controlSet = controlSet.add(interval);
            set = set.add(interval);
            assertTrue(set.contains(interval));
            assertEquals(controlSet.size(), set.size());
        }
        Seq<LongInterval> seq = controlSet.seq();
        while (!seq.isEmpty())
        {
            LongInterval interval = seq.first();
            assertTrue(set.contains(interval));
            assertSame(controlSet.find(interval), set.find(interval));
            seq = seq.rest();
            set = set.remove(interval);
            controlSet = controlSet.remove(interval);
            assertFalse(set.contains(interval));
            assertEquals(controlSet.size(), set.size());
        }
        assertEquals(0, set.size());
    }

    public void testRunPointContainment()
    {
        Random rnd = new Random();
        rnd.setSeed(randomSeed);
        ExtendibleArray<LongInterval> unsorted = new ExtendibleArray<LongInterval>();
        IntervalSet<Long, LongInterval> set = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            long a = rnd.nextInt(N) - 2;
            long b = a + rnd.nextInt(10);
            LongInterval interval = new LongInterval(a, b);
            unsorted.add(interval);
            set = set.add(interval);
        }
        Collections.sortUnique(unsorted.asConstant(), IntervalSet.intervalComperator);
        int inc = 2;
        for (long point = -10; point < N + 10; point += inc)
        {
            set.containing(point);
        }
    }

    public void testPointContainment()
    {
        Random rnd = new Random();
        rnd.setSeed(randomSeed);
        ExtendibleArray<LongInterval> unsorted = new ExtendibleArray<LongInterval>();
        IntervalSet<Long, LongInterval> set = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            long a = rnd.nextInt(N) - 2;
            long b = a + rnd.nextInt(10);
            LongInterval interval = new LongInterval(a, b);
            unsorted.add(interval);
            set = set.add(interval);
        }
        Array<LongInterval> sorted = Collections.sortUnique(unsorted.asConstant(), IntervalSet.intervalComperator);
        Array<LongInterval> containing = set.containing(2L);
        assertEquals(containingIntervals(sorted, 2L), containing);
        int inc = 2;
        for (long point = -10; point < N + 10; point += inc)
        {
            containing = set.containing(point);
            assertEquals("failure at point " + point, containingIntervals(sorted, point), containing);
        }
    }

    private Array<LongInterval> containingIntervals(Array<LongInterval> intervals, long point)
    {
        ExtendibleArray<LongInterval> result = new ExtendibleArray<LongInterval>();
        ForwardIterator<LongInterval> it = intervals.iterator();
        while (it.hasNext())
        {
            LongInterval interval = it.next();
            if (interval.contains(point))
                result.add(interval);
        }
        return ConstantArray.fromCollection(result.asConstant());
    }

    public void testIntersections()
    {
        Random rnd = new Random();
        rnd.setSeed(randomSeed);
        ExtendibleArray<LongInterval> unsorted = new ExtendibleArray<LongInterval>();
        IntervalSet<Long, LongInterval> set = IntervalSet.create();
        for (int i = 0; i < N; i++)
        {
            long a = rnd.nextInt(N) - 2;
            long b = a + rnd.nextInt(10);
            LongInterval interval = new LongInterval(a, b);
            unsorted.add(interval);
            set = set.add(interval);
        }
        Array<LongInterval> sorted = Collections.sortUnique(unsorted.asConstant(), IntervalSet.intervalComperator);
        int inc = 2;
        for (long a = -10; a < N + 10; a += inc)
        {
            long b = a + rnd.nextInt(20);
            LongInterval interval = new LongInterval(a, b);
            Array<LongInterval> intersecting = set.intersections(interval);
            assertEquals("failure at interval " + interval, intersectingIntervals(sorted, interval), intersecting);
        }
    }

    private Array<LongInterval> intersectingIntervals(Array<LongInterval> intervals, LongInterval interval)
    {
        ExtendibleArray<LongInterval> result = new ExtendibleArray<LongInterval>();
        ForwardIterator<LongInterval> it = intervals.iterator();
        while (it.hasNext())
        {
            LongInterval nextInterval = it.next();
            if (nextInterval.intersects(interval))
                result.add(nextInterval);
        }
        return result.asConstant();
    }
}
