package org.gendut.memory;

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

		private static int depthOf(ListNode n) {
			return n == null ? 0 : n.depth;
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
				return next.equals(other.next);
			case 1:
				return value == other.value;
			default:
				throw new IndexOutOfBoundsException();
			}
		}

		@Override
		protected int getUnmanagedDepth() {
			return 16;
		}
	}

	UnifyingAllocator allocator = new UnifyingAllocator();

	static final int N = 500000;

	@Test
	public void testWithoutUnification() {
		for (int k = 0; k < 50; k++) {
			ListNode p = null;
			for (int i = 0; i < N; i++) {
				p = new ListNode(i, p);
				p.hashCode();
			}
		}
	}

	@Test
	public void testWithtUnification() {
		for (int k = 0; k < 50; k++) {
			ListNode p = null;
			for (int i = 0; i < N; i++) {
				p = new ListNode(i, p);
				p = (ListNode) allocator.alloc(p);
			}
		}
	}
}
