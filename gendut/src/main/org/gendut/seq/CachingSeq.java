package org.gendut.seq;

import org.gendut.iterator.IteratorFromSeq;

/**
 * Turns a seq into a caching seq: This is useful if first() or rest() has some
 * heavy computational parts that should only be execute once.
 */
public class CachingSeq {
  private CachingSeq() {};
  
  public static<E> Seq<E> create(Seq<E> source) {
    return SeqFromIterator.create(new IteratorFromSeq<E>(source));
  }
}
