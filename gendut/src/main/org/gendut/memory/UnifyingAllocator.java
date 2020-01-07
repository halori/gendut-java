package org.gendut.memory;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class UnifyingAllocator {

	private final WeakHashMap<Object, WeakReference<Object>> memorizedObject = new WeakHashMap<Object, WeakReference<Object>>(
			1000);

	private final int maxDepth;

	/**
	 * a node is unified in storage if and only if its depth has reached the maximum depth
	 */
	public UnifyingAllocator(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public Object alloc(Object o) {
		if (!(o instanceof Node) || maxDepth == 0)
			return o;

		Node n = (Node) o;
		if (n.depth == maxDepth)
			return memorizeObject(n);
		else
			return n;

	}

	private Object memorizeObject(Object o) {
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
