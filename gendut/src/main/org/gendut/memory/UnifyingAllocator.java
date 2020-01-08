package org.gendut.memory;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class UnifyingAllocator {

	//TODO: use array of 8 weahHashMaps, one for each hash % 8.
	private final WeakHashMap<Object, WeakReference<Object>> memorizedObject = new WeakHashMap<Object, WeakReference<Object>>(
			1000);

	

	public Object alloc(Object o) {
		if (!(o instanceof ManagedNode))
			return o;

		ManagedNode n = (ManagedNode) o;
		if (n.depth % n.getUnmanagedDepth() == 0)
			return memorizeObjects(n);
		else
			return n;

	}

	private Object memorizeObjects(Object o) {
		synchronized (memorizedObject) {
			if (memorizedObject.containsKey(o)) {
				return memorizedObject.get(o).get();
			} else {
				memorizedObject.put(o, new WeakReference<Object>(o));
				return o;
			}
		}
	}
}
