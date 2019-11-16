package org.gendut.collection;

import org.gendut.algorithm.Comparator;
import org.gendut.arithmetic.Int;
import org.gendut.func.Function;

/**
 * Large, catenable and splitable array implementation with homomorphic images.
 */
public final class LargeArray<E> extends CatenableArrayTree<E> implements
    CatenableArray<E> {

  <F> LargeArray(Object root, ImmutableHashMap<MonoidMap<E, ?>, Object> images,
      Comparator<? super E> cmp) {
    super(root, images, cmp);
  }

  public LargeArray() {
    this(emptyTree);
  }

  /**
   * Package-private for testing purpose
   */
  LargeArray(Object tree) {
    super(tree, null, null);
  }

  public LargeArray(CatenableArrayTree<E> arr1, CatenableArrayTree<E> arr2) {
    super(arr1, arr2);
  }

  public LargeArray<E> catenate(SortedSet<? extends E> array) {
    return (LargeArray<E>) super.catenate(array);
  }

  public LargeArray<E> catenate(SortedCollection<? extends E> array) {
    return (LargeArray<E>) super.catenate(array);
  }

  //TODO: same with maintaining images
  public LargeArray<E> catenate(LargeArray<? extends E> array) {
    return (LargeArray<E>) super.catenate(array);
  }

  public LargeArray<E> subArray(Int start, Int end) {
    // TODO Auto-generated method stub
    return null;
  }
  
  final public Int lastOf(final E e) {
    return lastOf(new Function<E, Boolean>() {
      @Override
      public Boolean get(E f) {
        return f.equals(e);
      }
    });
  }

  final public Int firstOf(final E e) {
     return firstOf(new Function<E, Boolean>() {
		@Override
		public Boolean get(E f) {
			return f.equals(e);
		}
	});
  }

  public LargeArray<E> insertAt(Int pos, E e) {
	  return new LargeArray<E>(insertAt(root, pos, e));
  }

  public LargeArray<E> replaceAt(Int pos, E e) {
    return new LargeArray<E>(replace(root, pos, e));
  }

  public LargeArray<E> removeAt(Int pos) {
    // TODO Auto-generated method stub
    return null;
  }

  // TODO: traverse graph instead!
  public E find(E e) {
    return Collections.findViaIterator(this, e);
  }

  // TODO: traverse graph instead!
  public boolean contains(E e) {
    return Collections.containsViaIterator(this, e);
  }

  public LargeArray<E> insertAt(long pos, E e) {
    return insertAt(Int.create(pos), e);
  }

  public LargeArray<E> removeAt(long pos) {
    return removeAt(Int.create(pos));
  }

  public LargeArray<E> replaceAt(long pos, E e) {
    return replaceAt(Int.create(pos), e);
  }

  public LargeArray<E> subArray(long start, long end) {
    return subArray(Int.create(start), Int.create(end));
  }

  public LargeArray<E> addMap(MonoidMap<E, ?> map) {
    if (images.containsKey(map))
      return this;
    else
      return new LargeArray<E>(root, images.put(map, computeImageTree(root, map)), cmp);
  }

  public LargeArray<E> removeMap(MonoidMap<E, ?> map) {
    return new LargeArray<E>(root, images.remove(map), cmp);
  }

  public LargeArray<E> add(E e) {
    return new LargeArray<E>(concat(root, e, null, null, null, null), images, cmp);
  }

  public LargeArray<E> clear() {
    return new LargeArray<E>(emptyTree, emptyImages(images.keys()), cmp);
  }

  public LargeArray<E> remove(E e) {
    // TODO
    return null;
  }
}
