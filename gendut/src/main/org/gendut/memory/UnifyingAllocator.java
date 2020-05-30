package org.gendut.memory;

public final class UnifyingAllocator {

	//TODO: use array of 8 weahHashMaps, one for each hash % 8.
	private final SingleAllocator memorizedObjects = new SingleAllocator(10000);

	

	public Object alloc(Object o) {
		if (!(o instanceof ManagedNode))
			return o;

		ManagedNode<?> n = (ManagedNode<?>) o;
		if (n.depth % n.getUnmanagedDepth() == 0)
			return memorizedObjects.unifiedAlloc(n);
		else
			return n;

	}
}
