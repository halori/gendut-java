package org.gendut.collection;

import junit.framework.TestCase;

import org.gendut.func.Function;

public class LazySeqTest extends TestCase {

  
  public void testFiniteList() {
    LazySeq<Integer> list = null;
    list = new LazySeq<Integer>(3, list);
    list = new LazySeq<Integer>(2, list);
    list = new LazySeq<Integer>(1, list);
    assertEquals(Integer.valueOf(1), list.first(0));
    assertEquals(Integer.valueOf(2), list.first(1));
    assertEquals(Integer.valueOf(3), list.first(2));
    try {
      list.first(3);
      fail("expected IndexOutOfBoundsException");
    } 
    catch (IndexOutOfBoundsException e) {  
    }
    assertEquals(Integer.valueOf(2), list.rest(0).first(0));
    assertEquals(Integer.valueOf(3), list.rest(1).first(0));
    assertEquals(Integer.valueOf(3), list.rest(0).first(1));
    assertEquals(LazySeq.empty(), list.rest(2));
    try {
      list.rest(3);
      fail("expected IndexOutOfBoundsException");
    } 
    catch (IndexOutOfBoundsException e) {  
    }
    
  }
  
	static final int chunkSize = 16;

	static Function<Integer, LazySeq<Integer>> nextIntegersLazyRest = new Function<Integer, LazySeq<Integer>>() {

		public LazySeq<Integer> get(Integer chunkStart) {
			int chunkEnd = chunkStart + chunkSize;
			int i = chunkEnd - 1;
			LazySeq<Integer> list = new LazySeq<Integer>(i, nextIntegersLazyRest, chunkEnd);
			i = i - 1;
			while (i >= chunkStart) {
				list = new LazySeq<Integer>(i, list);
				i = i - 1;
			}
			return list;
		}

	};

	public void testGenerateNumbersLazyRest() {
		LazySeq<Integer> integers = nextIntegersLazyRest.get(0);
		for (Integer i = 0; i < 100; i++) {
			assertEquals(i, integers.first(0));
			assertEquals(Integer.valueOf(i + 12), integers.first(12));
			assertEquals(Integer.valueOf(i + 13), integers.rest(12).first(0));
			assertEquals(Integer.valueOf(i + 120), integers.first(120));
			assertEquals(Integer.valueOf(i + 121), integers.rest(120).first(0));
			integers = integers.rest(0);
		}
	}

	static Function<Integer, Integer> increment = new Function<Integer, Integer>() {

		public Integer get(Integer i) {
			return i + 1;
		}
	};

	static Function<Integer, LazySeq<Integer>> nextIntegersLazyFirstAndRest = new Function<Integer, LazySeq<Integer>>() {

		public LazySeq<Integer> get(Integer chunkStart) {
			int chunkEnd = chunkStart + chunkSize;
			int i = chunkEnd - 1;
			LazySeq<Integer> list = new LazySeq<Integer>(increment, i, nextIntegersLazyFirstAndRest, chunkEnd);
			i = i - 1;
			while (i >= chunkStart) {
				list = new LazySeq<Integer>(increment, i, list);
				i = i - 1;
			}
			return list;
		}
	};

	public void testGenerateNumbersLazyFirstAndRest() {
		LazySeq<Integer> integers = nextIntegersLazyFirstAndRest.get(0);
		for (Integer i = 0; i < 100; i++) {
			assertEquals(Integer.valueOf(i + 1), integers.first(0));
			assertEquals(Integer.valueOf(i + 13), integers.first(12));
			assertEquals(Integer.valueOf(i + 14), integers.rest(12).first());
			assertEquals(Integer.valueOf(i + 121), integers.first(120));
			assertEquals(Integer.valueOf(i + 122), integers.rest(120).first());
			integers = integers.rest();
		}
	}
}
