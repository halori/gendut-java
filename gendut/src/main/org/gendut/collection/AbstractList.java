package org.gendut.collection;

import java.math.BigInteger;

import org.gendut.iterator.ForwardIterator;


abstract public class AbstractList<E> extends AbstractCollection<E> implements List<E>
{
    @Override
    public long size()
    {
        return elementCount().longValue();
    }

    /**
     * This method may not return for infinite lists
     */
    @Override
    public BigInteger elementCount()
    {
        ForwardIterator<?> it = iterator();
        long cnt = 0;
        while (it.hasNext() && cnt < Long.MAX_VALUE / 2)
        {
            cnt++;
            it.next();
        }
        BigInteger count = BigInteger.valueOf(cnt);
        while (it.hasNext())
        {
            count = count.add(BigInteger.ONE);
            it.next();
        }
        return count;
    }

    @Override
    public boolean contains(E e)
    {
        return Collections.containsViaIterator(this, e);
    }

    @Override
    public E find(E e)
    {
        return Collections.findViaIterator(this, e);
    }

    @Override
    public int hashCode()
    {
        return Collections.hashCodeForLists(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return Collections.equalsForList(this, obj);
    }

    @Override
    final public String toString()
    {
        return Collections.toStringIterationOrder(this);
    }
}
