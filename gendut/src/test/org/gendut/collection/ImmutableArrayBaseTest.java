package org.gendut.collection;

import java.util.ArrayList;

import org.gendut.arithmetic.Int;
import org.gendut.collection.ImmutableArray;
import org.gendut.iterator.ForwardIterator;

import junit.framework.TestCase;

public class ImmutableArrayBaseTest extends TestCase {
	static int N = 2700;

	static public void testBase() {

		ImmutableArray<Integer> tuple = ImmutableArray.create(N);
		assertEquals(N, tuple.size());
		for (int i = 0; i < N; i++) {
			tuple = tuple.set(i, i);
			for (int j = Math.max(0, i - 14); j <= i; j++) {
				assertEquals(j, (int) tuple.get(j));
			}
		}

		ImmutableArray<Integer> tupleFull = tuple;

		for (int k = 0; k < 2; k++) {
			tuple = tupleFull;
			for (int i = 0; i < N; i++) {
				assertEquals(i, (int) tuple.get(i));
				tuple = tuple.set(i, null);
				assertNull(tuple.get(i));
			}
		}
	}

	static long start = 5000000000L;

	static public void testVeryLargeTuple() {

		ImmutableArray<Integer> tuple = ImmutableArray.create(start + N);
		assertEquals(start + N, tuple.size());
		for (int i = 0; i < N; i++) {
			tuple = tuple.set(start + i, i);
			for (int j = Math.max(0, i - 14); j <= i; j++) {
				assertEquals(j, (int) tuple.get(start + j));
			}
		}
	}

	static public void testAgainstArrayList() {
		ArrayList<Integer> tuple = new ArrayList<Integer>();
		for (int i = 0; i < N; i++)
			tuple.add(null);

		assertEquals(N, tuple.size());
		for (int i = 0; i < N; i++) {
			tuple.set(i, i);
			for (int j = Math.max(0, i - 14); j <= i; j++) {
				assertEquals(j, (int) tuple.get(j));
			}
		}

		ArrayList<Integer> tupleFull = new ArrayList<Integer>(tuple);

		for (int k = 0; k < 2; k++) {
			tuple = new ArrayList<Integer>(tupleFull);
			for (int i = 0; i < N; i++) {
				assertEquals(i, (int) tuple.get(i));
				tuple.set(i, null);
				assertNull(tuple.get(i));
			}
		}
	}

	public void testNotNullIterator() {
		ImmutableArray<Integer> array = ImmutableArray.create(N);
		assertEquals(N, array.size());
		ForwardIterator<Integer> it = array.notNullIterator();
		assertFalse(it.hasNext());

		array = array.set(0, 0);
		it = array.notNullIterator();
		assertTrue(it.hasNext());
		assertEquals(0, (int) it.next());
		assertFalse(it.hasNext());

		array = array.set(2, 2);
		it = array.notNullIterator();
		assertTrue(it.hasNext());
		assertEquals(0, (int) it.next());
		assertTrue(it.hasNext());
		assertEquals(2, (int) it.next());
		assertFalse(it.hasNext());

		array = array.set(200, 200);
		it = array.notNullIterator();
		assertTrue(it.hasNext());
		assertEquals(0, (int) it.next());
		assertTrue(it.hasNext());
		assertEquals(2, (int) it.next());
		assertTrue(it.hasNext());
		assertEquals(200, (int) it.next());
		assertFalse(it.hasNext());

		for (int i = 0; i < 100; i++)
			array = array.set(i, i);

		it = array.notNullIterator();
		for (int i = 0; i < 100; i++)
			assertEquals(i, (int) it.next());
	}

	public void testDifferences() {
		int N = 10000;
		ImmutableArray<Int> A = ImmutableArray.create(N);
		for (int i = 0; i < N / 2; i++) {
			A = A.set(i, Int.create(i));
		}
		ImmutableArray<Int> B = A;
		assertTrue(A.differentPositions(B).isEmpty());
		
		B = B.set(120, Int.create(0));
		B = B.set(9125, Int.create(0));
		ConstantArray<Int> differences = A.differentPositions(B);
		assertEquals("[120, 9125]", differences.toString());
		
		B = B.set(0,  Int.create(14));
		differences = A.differentPositions(B);
		assertEquals("[0, 120, 9125]", differences.toString());
	}
}
