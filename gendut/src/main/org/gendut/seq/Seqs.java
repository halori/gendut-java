package org.gendut.seq;

import java.math.BigInteger;

import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;


public class Seqs
{
    private Seqs()
    {
    };

    public static <T> Seq<T> appendBefore(T e, Seq<T> seq)
    {
        return new ElementBeforeSeq<T>(e, seq);
    }

    private static class ElementBeforeSeq<T> implements Seq<T>
    {
        final T first;

        final Seq<T> rest;

        public ElementBeforeSeq(T first, Seq<T> rest)
        {
            this.first = first;
            this.rest = rest;
        }

        @Override
        public ForwardIterator<T> iterator()
        {
            return new IteratorFromSeq<T>(this);
        }

        @Override
        public T first()
        {
            return first;
        }

        @Override
        public Seq<T> rest()
        {
            return rest;
        }

        @Override
        public boolean isEmpty()
        {
            return false;
        }
    }

    public static <T> BigInteger size(Seq<T> seq)
    {
        BigInteger size = BigInteger.ZERO;
        while (!seq.isEmpty())
        {
            size = size.add(BigInteger.ONE);
            seq = seq.rest();
        }
        return size;
    }
}
