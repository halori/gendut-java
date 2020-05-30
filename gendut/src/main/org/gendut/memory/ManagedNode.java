package org.gendut.memory;


public abstract class ManagedNode<N extends ManagedNode<N>> {
	
	/**
	 * The depth must be one more than the largest depth of its children, if it has
	 * any. If there aren't any, the depth can be arbitrary. For instance, start
	 * with a value > 0 if you want to exclude small structures from being cached.
	 */
	public final long depth;
	public final int hash;

	public ManagedNode(long depth, int hash) {
		this.depth = depth;
		this.hash = hash;
	}

	public final int hashCode() {
		return hash;
	}

	protected abstract int getNumberOfChildren();

	protected abstract Object getChild(int i);

	/**
	 * only nodes which depth is divided by the unmanaged depth are cached. Its main
	 * purpose is to speed up structural comparison. The cache consumes extra memory
	 * but may, in some cases, unify equal structures and then save space. Its major
	 * drawback is that it slows down the allocation of managed nodes.
	 */
	protected abstract int getUnmanagedDepth();

	protected String getChildName(int i) {
		return "" + i;
	}

	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("(");
		for (int i = 0; i < getNumberOfChildren(); i++) {
			if (i == 0)
				text.append(",");
			text.append(getChildName(i));
			text.append("=");
			text.append(getChild(i).toString());
		}
		text.append(")");
		return text.toString();
	}

	/**
	 * child objects must be compared by identity(!). You can overwrite this method in
	 * order to speed up comparison of primitive elements. This method is typically
	 * called during the allocation of managed nodes, which is not reentrant. Therefore it
	 * is generally a bad idea to refer to black-box implementations of equals, (of
	 * generically typed elements, for instance) when you overwrite this method.
	 */
	public boolean equalsForChild(int i, N other) {
		Object c1 = getChild(i);
		Object c2 = other.getChild(i);
		if (c1 == null)
			return c2 == null;
		if (c1 instanceof ManagedNode) {
			return c1.equals(c2);
		}
		return c1 == c2;
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}
	
	public final boolean equals(Object obj, boolean allocated) {

		if (this == obj)
			return true;
		if (depth % getUnmanagedDepth() == 0 && allocated)
			return this == obj;
		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		N other = (N) obj;

		if (this.hashCode() != other.hashCode() || depth != other.depth)
			return false;

		int childCount = getNumberOfChildren();
		if (other.getNumberOfChildren() != childCount)
			return false;

		for (int i = 0; i < childCount; i++) {
			if (!equalsForChild(i, other))
				return false;
		}
		return true;
	}
}
