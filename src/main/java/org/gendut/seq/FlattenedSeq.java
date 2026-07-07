package org.gendut.seq;

import org.gendut.collection.AbstractList;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;


/**
 * A flattened seq takes a sequence of sequences of the same type and represents the sequence of
 * their elements as a flat sequence.
 * 
 */
public class FlattenedSeq<N> extends AbstractList<N> implements Seq<N>
{
    private final Seq<N> headSeq;

    private final Seq<Seq<N>> restSeq;

    public static <N> Seq<N> create(Seq<Seq<N>> from)
    {
        return new FlattenedSeq<N>(from);
    }

    private FlattenedSeq(Seq<Seq<N>> from)
    {
        Seq<Seq<N>> trimmedSeq = deleteEmptyHeads(from);
        if (trimmedSeq.isEmpty())
        {
            headSeq = null;
            restSeq = null;
        }
        else
        {
            headSeq = trimmedSeq.first();
            restSeq = trimmedSeq.rest();
        }
    }

    private static <N> Seq<Seq<N>> deleteEmptyHeads(Seq<Seq<N>> seq)
    {
        while (!seq.isEmpty() && seq.first().isEmpty())
            seq = seq.rest();
        return seq;
    }

    private FlattenedSeq(Seq<N> headSeq, Seq<Seq<N>> restSeq)
    {
        this.headSeq = headSeq;
        this.restSeq = restSeq;
    }

    @Override
    public N first()
    {
        return headSeq.first();
    }

    @Override
    public FlattenedSeq<N> rest()
    {
        Seq<N> restOfHead = headSeq.rest();
        if (restOfHead.isEmpty())
            return new FlattenedSeq<N>(restSeq);
        else return new FlattenedSeq<N>(restOfHead, restSeq);
    }

    @Override
    public boolean isEmpty()
    {
        return headSeq == null;
    }

    @Override
    public ForwardIterator<N> iterator()
    {
        return new IteratorFromSeq<N>(this);
    }
}