package org.gendut.collection;

public interface UpdatableCollection<E, ReturnType extends UpdatableCollection<E, ReturnType>>
    extends ImmutableCollection<E> {
  /**
   * adds an element to the collection without consideration of other equal
   * elements already contained.
   */
  public ReturnType add(E e);

  /**
   * if the collection contains an element equal to e, exactly one of them will
   * be removed.
   */
  public ReturnType remove(E e);

  /**
   * removes all elements from the given collection
   */
  public ReturnType clear();
}
