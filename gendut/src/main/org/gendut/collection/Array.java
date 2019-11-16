package org.gendut.collection;

// ! List Interface
/* <literate> */
/**
 * Interface for a randomly accessible collection. The performance of get(i) should be efficient,
 * i.e. have O(1) or O(log n) time bounds
 */
public interface Array<E> extends List<E>
{
    E get(long i);
}
