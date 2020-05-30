package org.gendut.memory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import org.gendut.collection.mutable.MutableHashSet;

public final class SingleAllocator {

	private Exception cleanupException = null; 
	
	private ReferenceQueue<ManagedNode<?>> abandonnedRefs = new ReferenceQueue<ManagedNode<?>>();
	
	private MutableHashSet<NodeEntry> set;
	
	public SingleAllocator(int initialSize) {
		set = new MutableHashSet<>(initialSize);
		startCleanupThread();
	}

	@SuppressWarnings("unchecked")
	public<T extends ManagedNode<T>> ManagedNode<T> unifiedAlloc(ManagedNode<T> node) {
		
		if (cleanupException != null)
			throw new IllegalStateException("Cleanup thread died unexpectedly.", cleanupException);
		
		
		NodeEntry key = new NodeEntry(node);
		
		synchronized (set) {
			NodeEntry existingRef = set.find(key);
			ManagedNode<?> existingNode = existingRef == null ? null : existingRef.get();
			
			if (existingNode != null) {
				return (ManagedNode<T>) existingNode;
			}
			set.add(new NodeEntry(node, abandonnedRefs));
			return node;
		}
		
	}
	
	private static class NodeEntry extends WeakReference<ManagedNode<?>> {
		
		public final int hashcode;
		
		
		public NodeEntry(ManagedNode<?> referent, ReferenceQueue<ManagedNode<?>> q) {
			super(referent, q);
			hashcode = referent.hashCode();
		}
		
		public NodeEntry(ManagedNode<?> referent) {
			super(referent);
			hashcode = referent.hashCode();
		}
		
		@Override
		public int hashCode() {
			return hashcode;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (other == null || !(other instanceof NodeEntry))
				return false;
			
			
			ManagedNode<?> a = this.get();
			@SuppressWarnings("unchecked")
			ManagedNode<?> b = ((WeakReference<ManagedNode<?>>) other).get();
			
			return a == null ? b == null : a.equals(b, false);
		}
	}

	
	
	private void startCleanupThread() {
		

		Thread cleanUpThread = new Thread() {
			@Override
			public void run() {

				try {
				  while(true)
				  {  
						 Reference<?> ref = abandonnedRefs.remove();
				  
				   	    synchronized (set) {
				   	    	set.remove((NodeEntry)ref);			   	    	
				  }
				}
				
			} catch (InterruptedException e) {
				cleanupException = e;
			};
		}
		};
		
		cleanUpThread.start();
	}

}
