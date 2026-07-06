package org.gendut.memory;

public final class UnifyingAllocator {

	private SingleAllocator memorizedObjects[];
	
	private final int parallelCount;	
	
	public UnifyingAllocator(int parallelCount, int initialSize) {
		this.parallelCount = parallelCount;
		memorizedObjects = new SingleAllocator[parallelCount];
		for (int i = 0; i < parallelCount; i++) {
			memorizedObjects[i] = new SingleAllocator(initialSize);
		}
	}

	public Object alloc(Object o) {
		if (!(o instanceof ManagedNode))
			return o;

		ManagedNode<?> n = (ManagedNode<?>) o;
		if (n.depth % n.getUnmanagedDepth() == 0) {
			int index = n.hashCode();
			if (index < 0)
				index = -index;
			index = index % parallelCount;
			return memorizedObjects[index].unifiedAlloc(n);
		}
		else
			return n;

	}
}
