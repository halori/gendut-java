package org.gendut.collection;

import org.gendut.arithmetic.Int;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

public final class Range<E> extends AbstractList<E> {

  private Array<E> list;
  private long start;
  private long end;
  
  
  @SuppressWarnings("unchecked")
  private Range(Array<? extends E> list, long start, long end) {
    super();
    this.list = (Array<E>) list;
    this.start = start;
    this.end = end;
  }

  public E get(long i) {
    if ((i < 0) || (i > end - start))
      throw new IndexOutOfBoundsException("Index i must be within 0.."+(end - start));
    return list.get(start+i);
  }

  public boolean contains(E e) {
    return (Collections.containsViaIterator(this, e));
  }
  
  public E find(E e) {
	    return (Collections.findViaIterator(this, e));
	  }

  static private final class RangeIterator<E> implements ForwardIterator<E> {
    private final ForwardIterator<E> iterator;
    private final long end;
    private long pos = 0;
    
    
    
    public RangeIterator(ForwardIterator<E> iterator, long start, long end) {
      super();
      this.iterator = iterator;
      this.end = end;
      for (int i = 0; i < start; i++) {
        if (!iterator.hasNext())
          throw new IllegalArgumentException("Collection has not enough elements.");
        iterator.next();
        pos = pos + 1;
      }
    }

    public boolean hasNext() {
      return (pos < end);
    }
    

    public E next() {
      if (!iterator.hasNext())
        throw new IllegalArgumentException("Collection has not enough elements.");
      pos = pos + 1;
      return iterator.next();
    }
    
    
  }
  
  public ForwardIterator<E> iterator() {
    return new RangeIterator<E>(list.iterator(), start, end);
  }

  public Seq<E> seq() {
    return SeqFromIterator.create(iterator());
  }

  public long size() {
    return end-start;
  }
  
	public Int elementCount() {
		return Int.create(size());
	}
  
  static public<E> Range<E> create(Array<? extends E> list, long start, long end) {
    return new Range<E>(list, start, end);
  }  
}
