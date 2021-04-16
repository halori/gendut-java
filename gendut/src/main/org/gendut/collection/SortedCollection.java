package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;

public final class SortedCollection<E> extends CatenableArrayTree<E> implements UpdatableCollection<E, SortedCollection<E>>, CatenableArray<E> {

    <F> SortedCollection(Object root, ImmutableHashMap<MonoidMap<E, ?>, Object> images, Comparator<? super E> cmp) {
        super(root,  images, cmp);
    }
    
    protected SortedCollection(CatenableArrayTree<E> arr1, CatenableArrayTree<E> arr2) {
        super(arr1, arr2);
        if (cmp.compare(arr1.last(), arr2.first()) > 0)
            throw new IllegalArgumentException("Sorted collections cannot be concatenated.");
    }

    public SortedCollection(Object root, Comparator<? super E> cmp) {
        super(root, null, cmp);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static public <E> SortedCollection<E> create(Comparator<? super E> cmp) {
        return new SortedCollection(emptyTree, cmp);
    }

    public SortedCollection<E> add(E e) {
        return new SortedCollection<E>(insert(root, cmp, e, allowCopies), cmp);
    }

    public SortedCollection<E> remove(E e) {
        return new SortedCollection<E>(remove(root, cmp, e), cmp);
    }

    public SortedCollection<E> clear() {
        return new SortedCollection<E>(emptyTree, emptyImages(images.keys()), cmp);
    }

    public SortedCollection<E> catenate(SortedSet<? extends E> array) {
        return (SortedCollection<E>) super.catenate(array);
    }

    public SortedCollection<E> catenate(SortedCollection<? extends E> array) {
        return (SortedCollection<E>) super.catenate(array);
    }

    public LargeArray<E> catenate(LargeArray<? extends E> array) {
        return (LargeArray<E>) super.catenate(array);
    }
    
    public SortedCollection<E> subArray(Int start, Int end) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedCollection<E> insertAt(Int pos, E e) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedCollection<E> replaceAt(Int pos, E e) {
        if (pos.compareTo(0) < 0 || pos.compareTo(elementCount()) >= 0)
            throw new IndexOutOfBoundsException();
        
        if (pos.compareTo(0) >= 0 && cmp.compare(get(pos.subtract(1)), e) > 0)
            throw new IllegalArgumentException("Predecessor is larger than than new element.");
        if (pos.compareTo(elementCount()) < 0 && cmp.compare(e, get(pos.add(1))) > 0)
            throw new IllegalArgumentException("Successor is smaller than new element.");
        
        return new SortedCollection<E>(replace(root, pos, e), cmp);
    }

    public boolean contains(E e) {
        return findFirst(root, cmp, e) != noElementFound;
    }

    @SuppressWarnings("unchecked")
    public E find(E e) {
        Object found = findFirst(root, cmp, e);
        if (found == noElementFound)
            return null;
        return (E) found;
    }
    
    final public Int lastOf(E e) {
      // TODO Auto-generated method stub
      return null;
    }

    final public Int firstOf(E e) {
      // TODO Auto-generated method stub
      return null;
    }
    
    public SortedCollection<E> removeAt(Int pos) {
    //TODO
        return null;
    }
    
    public SortedCollection<E> insertAt(long pos, E e) {
        return insertAt(Int.create(pos),e);
    }
 
    public SortedCollection<E> removeAt(long pos) {
        return removeAt(Int.create(pos));
    }

    public SortedCollection<E> replaceAt(long pos, E e) {
        return replaceAt(Int.create(pos), e);
    }

    public SortedCollection<E> subArray(long start, long end) {
        return subArray(Int.create(start), Int.create(end));
    }
    
    public SortedCollection<E> addMap(MonoidMap<E, ?> map) {
   	 if (images.containsKey(map))
   	      return this;
   	    else
   	      return new SortedCollection<E>(root, images.put(map, computeImage(map)), cmp);
   }

   public SortedCollection<E> removeMap(MonoidMap<E, ?> map) {
       return new SortedCollection<E>(root, images.remove(map), cmp);
   }
}
