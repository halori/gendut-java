package org.gendut.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnifyingAllocatorTest {

	public static class ListNode extends ManagedNode {

		public final Integer value;
		public final ListNode next;

		public ListNode(Integer value, ListNode next) {
			super(Integer.MAX_VALUE);
			this.value = value;
			this.next = next;
		}

		@Override
		protected int getNumberOfChildren() {
			return 2;
		}

		@Override
		protected Object getChild(int i) {
			switch (i) {
			case 0:
				return value;
			case 1:
				return next;
			default:
				throw new IndexOutOfBoundsException();
			}
		}
	}

	UnifyingAllocator allocator = new UnifyingAllocator(8);

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
