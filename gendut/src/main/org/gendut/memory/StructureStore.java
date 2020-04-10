package org.gendut.memory;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class StructureStore {
	 
	WeakHashMap<ManagedNode, WeakReference<ManagedNode>> table = new WeakHashMap<>();
	ReadWriteLock tableLock = new ReentrantReadWriteLock();
	
	public ManagedNode allocate(ManagedNode o) {
		return o;
	}
	
	public boolean isStructurallyEqual(ManagedNode o1, ManagedNode o2) {
		if (o1 == null)
			return (o2 == null);
		if (o2 == null) 
			return false;
		if (o1.getClass() != o2.getClass())
			return false;
		if (o1.getNumberOfChildren() != o2.getNumberOfChildren())
			return false;
		if (o1.equalsData(o2))
			return false;
		
		//TODO simultaneous iteration like in AVLTree diff
		
		return true;
	}
}
