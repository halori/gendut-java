package org.gendut.memory;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
	
public final class SingleAllocator {

	
	private WeakHashMap<ManagedNode<?>, WeakReference<ManagedNode<?>>> memorizedNodes;
	
	public SingleAllocator(int initialSize) {
		memorizedNodes = new WeakHashMap<>(initialSize);
	}

	@SuppressWarnings("unchecked")
	public<T extends ManagedNode<T>> ManagedNode<T> unifiedAlloc(ManagedNode<T> node) {
		
		synchronized (memorizedNodes) {
			 WeakReference<ManagedNode<?>> entry = memorizedNodes.get(node);
			 if (entry != null) {
				 return (ManagedNode<T>) entry.get();
			 } else {
				 memorizedNodes.put(node, new WeakReference<ManagedNode<?>>(node)); 
				 return node;
			 }
		}
	
	}
}
