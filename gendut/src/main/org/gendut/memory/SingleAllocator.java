package org.gendut.memory;

import java.lang.ref.ReferenceQueue;

import org.gendut.collection.mutable.MutableHashMap;

public final class SingleAllocator {

	private final Object lock = new Object();
	
	private ReferenceQueue<ManagedNode<?>> abandonnedRefs;
	
	private MutableHashMap<ManagedNode<?>, Boolean> set;
	
	public SingleAllocator(int initialSize) {
		set = new MutableHashMap<>(initialSize, abandonnedRefs);
		startCleanupThread();
	}

	public<T extends ManagedNode<T>> ManagedNode<T> unifiedAlloc(ManagedNode<T> node) {
		
		ManagedNode<T> exitingInstance = (ManagedNode<T>) set.findKey(node);
		
		if (exitingInstance != null)
			return exitingInstance;
		
		set.put(node, Boolean.TRUE);
		return node;
	}
	
	
	private void startCleanupThread() {
		// TODO Auto-generated method stub
		
	}
}
