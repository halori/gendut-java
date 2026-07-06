package org.gendut.iterator;

//!Forward Iterator Interface
/*<literate>*/
/**
 * Interface for an Iterator. It is a non-serializable, read-only
 * replacement for java.util.Iterator.
 */
public interface ForwardIterator<E> {
  E next();
  boolean hasNext();
}//`interface`
