package org.gendut.memory;

public abstract class ManagedNode<N extends ManagedNode<N>> {

	public final int depth;
	public final int hash;

	public ManagedNode(int depth, int hash) {
		this.depth = depth;
		this.hash = hash;
	}

	public int hashcode() {
		return hash;
	}

	protected abstract int getNumberOfChildren();

	protected abstract Object getChild(int i);

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
	public final boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (depth % getUnmanagedDepth() == 0)
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
