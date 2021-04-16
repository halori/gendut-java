package org.gendut.seq;

import junit.framework.TestCase;

import java.util.Comparator;

import org.gendut.collection.ConstantArray;
import org.gendut.collection.Stack;
import org.gendut.collection.mutable.ExtendibleArray;


public class MergedSortedSeqTest extends TestCase {

	Comparator<Integer> cmp = new Comparator<Integer>() {

		@Override
		public int compare(Integer a, Integer b) {
			return a.compareTo(b);
		}
		
	};
	
	public void testSingleSeq() {
		Seq<Integer> seq0 = ConstantArray.fromArray(new int[]{1, 2, 3 ,4});
		
		ExtendibleArray<Seq<Integer>> seqs = new ExtendibleArray<Seq<Integer>>();
		
		seqs.add(seq0);
		
	    Seq<Integer> merged = MergedSortedSeqs.create(seqs.asConstant(), cmp);
		
		assertEquals("1, 2, 3, 4", lineFrom(merged));
	}
	
	public void testManySeq() {
		ExtendibleArray<Seq<Integer>> seqs = new ExtendibleArray<Seq<Integer>>();
		
		Seq<Integer> seq0 = ConstantArray.fromArray(new int[]{2, 3 ,4});
		Seq<Integer> seq1 = ConstantArray.fromArray(new int[]{3 ,4});
		Seq<Integer> seq2 = ConstantArray.fromArray(new int[]{1, 2, 6 ,9});
		Seq<Integer> seq3 = ConstantArray.fromArray(new int[]{3, 5, 8 , 12});
		
		
		seqs.add(seq0);
		seqs.add(seq1);
		seqs.add(seq2);
		seqs.add(seq3);
		
	    Seq<Integer> merged = MergedSortedSeqs.create(seqs.asConstant(), cmp);
		
		assertEquals("1, 2, 2, 3, 3, 3, 4, 4, 5, 6, 8, 9, 12", lineFrom(merged));
	}
	
	
	public void testEmptySeq() {
		Seq<Integer> merged = Stack.create();
		assertEquals("", lineFrom(merged ));
	}
		
	

	private String lineFrom(Seq<Integer> seq) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		while (!seq.isEmpty()) {
			if (!first)
				text.append(", ");
			else
				first = false;
			
			text.append(seq.first());
			seq = seq.rest();
		}
		return text.toString();
	}
}
