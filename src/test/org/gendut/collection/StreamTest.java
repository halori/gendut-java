package org.gendut.collection;

import junit.framework.TestCase;

public class StreamTest extends TestCase {

	public void testConcatenation() {
		ConstantArray<Integer> arr = ConstantArray.fromArray(new int[] { 1, 2, 3 });
		Stream<Integer> s = arr.stream();
		s = s.concat(s);
		s = s.concat(s);
		assertEquals("[1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3]", s.toArray().toString());
	}
}
