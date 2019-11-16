package org.gendut.iterator;

import org.gendut.seq.Seq;

//!Iterator Wrapper for Sequences
/*<literate>*/
/**
 * Iterator wrapper for sequences.
 */
public final class IteratorFromSeq<E> implements ForwardIterator<E> {
  private Seq<E> S;
  public IteratorFromSeq(Seq<E> S) { this.S = S; }
  public E next() {
    E x = S.first();
    S = S.rest();
    return x;
  }
  
  public boolean hasNext() {
    return (!S.isEmpty());
  }
}//`class`
