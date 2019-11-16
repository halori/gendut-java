package org.gendut.collection;

import org.gendut.arithmetic.Int;
import org.gendut.func.Function;
import org.gendut.iterator.ForwardIterator;

public interface CatenableArray<E> extends Array<E> {
  public CatenableArray<E> catenate(CatenableArray<? extends E> array);

  public CatenableArray<E> catenateTo(CatenableArray<? extends E> array);

  public CatenableArray<E> subArray(Int start, Int end);

  public CatenableArray<E> subArray(long start, long end);

  public ForwardIterator<E> iterator(Int start);

  public ForwardIterator<E> iterator(long start);

  public E get(Int pos);

  public E get(long pos);

  /**
   * If the array is sorted array, the order relation will be used, otherwise we
   * can simply iterate over the array. If the array is very long (i.e. has a
   * lot of structural sharing), a more sophisticated graph-traversal should be
   * used.
   */
  public Int firstOf(E e);

  /**
   * If the array is sorted array, the order relation will be used, otherwise we
   * can simply iterate over the array. If the array is very long (i.e. has a
   * lot of structural sharing), a more sophisticated graph-traversal should be
   * used.
   */
  public Int lastOf(E e);

  /**
   * If the array is very long (i.e. has a lot of structural sharing), a more
   * sophisticated graph-traversal should be used.
   */
  public Int firstOf(Function<E, Boolean> condition);

  /**
   * If the array is very long (i.e. has a lot of structural sharing), a more
   * sophisticated graph-traversal should be used.
   */
  public Int lastOf(Function<E, Boolean> condition);

  public CatenableArray<E> insertAt(Int pos, E e);

  public CatenableArray<E> insertAt(long pos, E e);

  public CatenableArray<E> replaceAt(Int pos, E e);

  public CatenableArray<E> replaceAt(long pos, E e);

  public CatenableArray<E> removeAt(Int pos);

  public CatenableArray<E> removeAt(long pos);

  /**
   * returns the homomorhic image of the array. If the given homomorphic map is
   * part of the maintained maps, the image can be directly returned, otherwhise
   * the entire graph of the underlying tree is traversed.
   */
  public <F> F getImage(MonoidMap<E, F> map);

  public ImmutableSet<MonoidMap<E, ?>> getMaps();

  public CatenableArray<E> addMap(MonoidMap<E, ?> map);

  public CatenableArray<E> removeMap(MonoidMap<E, ?> map);
}
