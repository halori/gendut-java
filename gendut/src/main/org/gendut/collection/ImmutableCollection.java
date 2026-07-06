package org.gendut.collection;

import java.math.BigInteger;

import org.gendut.seq.Seq;


public interface ImmutableCollection<E> extends Seq<E>
{
    /**
     * Returns the size of the collection. In case that the size exceeds the maximum long value, an
     * IndexOutOfBoundsException is thrown. For most collections, this can never happen due to
     * memory restrictions. However, a list which fully-functionally catenable might have an
     * exponential size due to repeated "multiplication".
     */
    public long size() throws IndexOutOfBoundsException;

    /**
     * Returns the size of the collection.
     */
    public BigInteger elementCount();

    /**
     * Tests if the collection contains an equal object.
     */
    public boolean contains(E e);

    /**
     * Returns some object equal to the given object, and null if no such object is found.
     */
    public E find(E e);
    
    public Stream<E> stream();
}
