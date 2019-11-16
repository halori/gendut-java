package org.gendut.seq;

import org.gendut.collection.Collections;
import org.gendut.collection.ConstantArray;
import org.gendut.collection.Stack;

import junit.framework.TestCase;

public class FlattenedSeqTest extends TestCase {

	public void testFlattening() {
		ConstantArray<Integer> arr = ConstantArray.fromArray(new int[] { 1, 2, 3 });
		Stack<Seq<Integer>> seqs = Stack.create();
		seqs = seqs.push(arr);
		seqs = seqs.push(arr);
		seqs = seqs.push(arr);
		Seq<Integer> flatSeq = FlattenedSeq.create(seqs);
		assertEquals(9, Collections.size(flatSeq));
		assertEquals("[1, 2, 3, 1, 2, 3, 1, 2, 3]", flatSeq.toString());
	}
}
