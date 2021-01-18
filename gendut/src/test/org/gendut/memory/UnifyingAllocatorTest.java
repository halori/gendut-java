package org.gendut.memory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnifyingAllocatorTest {

	public static class ListNode extends ManagedNode<ListNode> {

		public final int value;
		public final ListNode next;

		public ListNode(Integer value, ListNode next) {
			super(depthOf(next) + 1, hashOf(value, next));
			this.value = value;
			this.next = next;
		}

		private static long depthOf(ListNode n) {
			return n == null ? 1 : n.depth;
		}

		@Override
		protected int getUnmanagedDepth() {
			return 16;
		}

		private static int hashOf(int value, ListNode next) {
			return Integer.hashCode(value) * 37 + 13 * (next == null ? 0 : next.hash);
		}

		@Override
		protected int getNumberOfChildren() {
			return 2;
		}

		@Override
		protected Object getChild(int i) {
			switch (i) {
			case 0:
				return next;
			case 1:
				return value;
			default:
				throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public boolean equalsForChild(int i, ListNode other) {
			switch (i) {
			case 0:
				return (next == null) ? other.next == null : next.equals(other.next);
			case 1:
				return value == other.value;
			default:
				throw new IndexOutOfBoundsException();
			}
		}
	}

	static final int LOOPS = 10000;
	static final int SIZE = 1000;

	static final UnifyingAllocator allocator = new UnifyingAllocator(8, 5);

	@Test
	public void testWithoutUnification() {
		for (int k = 0; k < LOOPS; k++) {
			ListNode p = null;
			for (int i = 0; i < SIZE; i++) {
				p = new ListNode(i, p);
				p.hashCode();
			}
		}
	}

	@Test
	public void testWithUnification() {
		for (int k = 0; k < LOOPS; k++) {
			ListNode p = null;
			for (int i = 0; i < SIZE; i++) {
				p = new ListNode(i, p);
				p = (ListNode) allocator.alloc(p);
			}
		}
	}

	@Test
	public void testWithUnificationAndCompare() {
		ListNode firstList = null;
		for (int k = 0; k < LOOPS; k++) {
			ListNode p = null;
			for (int i = 0; i < SIZE; i++) {
				p = new ListNode(i, p);
				p = (ListNode) allocator.alloc(p);
			}
			if (firstList != null) {
				assertTrue(firstList.equals(p));
				assertFalse(firstList.next.equals(p));
			}
			firstList = p;
		}
	}
}
