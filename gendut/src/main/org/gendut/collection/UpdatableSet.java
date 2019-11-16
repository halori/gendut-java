package org.gendut.collection;

public interface UpdatableSet<E, ReturnType extends UpdatableSet<E, ReturnType>>
		extends ImmutableSet<E>, UpdatableCollection<E, ReturnType> {
	public ReturnType add(E e);

	public ReturnType remove(E e);

	public ReturnType clear();

	public ReturnType minus(Set<? extends E> other);
}
