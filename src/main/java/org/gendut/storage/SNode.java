package org.gendut.storage;

public abstract class SNode<N extends SNode<N>> {
	volatile Shadow shadow;
	

	@Override
	public final boolean equals(Object obj) {
	

		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		N other = (N) obj;

		int childCount = getNumberOfChildren();
		if (depth != other.depth || other.getNumberOfChildren() != childCount || this.hashCode() != other.hashCode() || this.shadow != other.shadow)
			return false;


		if (depth % getUnmanagedDepth() == 0) {
			return this.shadow == other.shadow;
		}
				
		for (int i = 0; i < childCount; i++) {
			if (!equalsForChild(i, other))
				return false;
		}
		return true;
	}
	
	@Override
	public final int hashCode() {
		if (shadow == null) {
			Shadow.cast(this);
		}
		
		return shadow.hashCode();
	}

	/**
	 * child objects must be compared by identity(!). You can overwrite this method in
	 * order to speed up comparison of primitive elements. This method is typically
	 * called during the allocation of managed nodes, which is not reentrant. Therefore it
	 * is generally a bad idea to refer to black-box implementations of equals, (of
	 * generically typed elements, for instance) when you overwrite this method.
	 */
	@SuppressWarnings("rawtypes")
	public boolean equalsForChild(int i, N other) {
		Object c1 = getChild(i);
		Object c2 = other.getChild(i);
		if (c1 == null)
			return c2 == null;
		if (c1 instanceof Shadow) {
			return ((Shadow)c1).equals(c2);
		}
		return c1 == c2;
	}


	int depth;
	public abstract int getUnmanagedDepth();
	public abstract int getNumberOfChildren();
	public abstract Object getChild(int i);

}
