package org.gendut.collection;

public abstract class AbstractImmutable {

	private volatile int hashCode = -1;

	protected abstract int computeHash();

	public final int hashCode() {
		if (hashCode == -1)
			hashCode = computeHash();
		if (hashCode == -1)
			hashCode = -999;
		return hashCode;
	}

	public final boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;
		else
			return equalsForSameClass(obj);
	}

	public abstract boolean equalsForSameClass(Object other);

	public abstract String toString();
}
