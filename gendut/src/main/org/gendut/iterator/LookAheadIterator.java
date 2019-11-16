package org.gendut.iterator;

import java.util.NoSuchElementException;


//!Iterator Wrapper for Sequences
/*<literate>*/
/**
 * Iterator wrapper for sequences.
 */
public final class LookAheadIterator<E> implements ForwardIterator<E> {
  
  private ForwardIterator<E> S;
  private boolean finished = false;
  private E lookAhead; 
  
  public LookAheadIterator(ForwardIterator<E> S) { 
    this.S = S;
    if (S.hasNext())
      lookAhead = S.next();
    else
      finished = true;
  }
  
  public E next() {
    if (finished)
      throw new NoSuchElementException();
      
    E result = lookAhead;
    if (S.hasNext())
      lookAhead = S.next();
    else
      finished = true;
    return result;
    
  }
  
  public E peek() {
    return lookAhead;
  }
  
  public boolean hasNext() {
    return !finished;
  }
}//`class`
