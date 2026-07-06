package org.gendut.collection;

import java.math.BigInteger;
import java.util.Comparator;


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
    
    public SortedCollection<E> subArray(BigInteger start, BigInteger end) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedCollection<E> insertAt(BigInteger pos, E e) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedCollection<E> replaceAt(BigInteger pos, E e) {
        if (pos.compareTo(BigInteger.ZERO) < 0 || pos.compareTo(elementCount()) >= 0)
            throw new IndexOutOfBoundsException();
        
        if (pos.compareTo(BigInteger.ZERO) >= 0 && cmp.compare(get(pos.subtract(BigInteger.ONE)), e) > 0)
            throw new IllegalArgumentException("Predecessor is larger than than new element.");
        if (pos.compareTo(elementCount()) < 0 && cmp.compare(e, get(pos.add(BigInteger.ONE))) > 0)
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
    
    final public BigInteger lastOf(E e) {
      // TODO Auto-generated method stub
      return null;
    }

    final public BigInteger firstOf(E e) {
      // TODO Auto-generated method stub
      return null;
    }
    
    public SortedCollection<E> removeAt(BigInteger pos) {
    //TODO
        return null;
    }
    
    public SortedCollection<E> insertAt(long pos, E e) {
        return insertAt(BigInteger.valueOf(pos), e);
    }
 
    public SortedCollection<E> removeAt(long pos) {
        return removeAt(BigInteger.valueOf(pos));
    }

    public SortedCollection<E> replaceAt(long pos, E e) {
        return replaceAt(BigInteger.valueOf(pos), e);
    }

    public SortedCollection<E> subArray(long start, long end) {
        return subArray(BigInteger.valueOf(start), BigInteger.valueOf(end));
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
