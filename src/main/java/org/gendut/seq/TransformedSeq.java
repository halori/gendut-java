package org.gendut.seq;

import java.util.function.Function;

import org.gendut.collection.AbstractList;
import org.gendut.collection.mutable.MutableValue;
import org.gendut.func.LazyValue;
import org.gendut.func.Pair;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;


/**
 * A transformed sequence is the result of an element-wise mapping of some other sequence. It also
 * allows some filtering of the original sequence. If no filter is applied, all elements from the
 * original sequence are transformed.
 */
public class TransformedSeq<M, N> extends AbstractList<N> implements Seq<N>
{
    private final LazyValue<Pair<N, TransformedSeq<M, N>>> firstAndRest;

    /**
     * The constructor takes an original sequence and a mapping.
     */
    public static <M, N> Seq<N> create(Seq<M> from,
                    Function<? super M, ? extends N> map)
    {
        return new TransformedSeq<M, N>(from, x -> true, map);
    }

    /**
     * The constructor takes an original sequence, a filter for that sequence, and a mapping.
     */
    public static <M, N> Seq<N> create(Seq<M> from,
                    Function<? super M, Boolean> filter,
                    Function<? super M, ? extends N> map)
    {
        return new TransformedSeq<M, N>(from, filter, map);
    }

    private TransformedSeq(Seq<M> from, final Function<? super M, Boolean> filter,
                    final Function<? super M, ? extends N> map)
    {
        this.firstAndRest = new LazyValue<Pair<N, TransformedSeq<M, N>>>(
                        new Function<MutableValue<Seq<M>>, Pair<N, TransformedSeq<M, N>>>()
                        {
                            @Override
                            public Pair<N, TransformedSeq<M, N>> apply(MutableValue<Seq<M>> remaining)
                            {
                                Seq<M> in = remaining.get();
                                remaining.set(null);
                                while (!in.isEmpty())
                                {
                                    M next = in.first();
                                    if (filter.apply(next))
                                    {
                                        N first = map.apply(next);
                                        TransformedSeq<M, N> rest = new TransformedSeq<M, N>(in.rest(), filter, map);
                                        return Pair.create(first, rest);
                                    }
                                    in = in.rest();
                                }
                                return null;
                            }
                        }, new MutableValue<Seq<M>>(from));
    }

    @Override
    public N first()
    {
        Pair<N, TransformedSeq<M, N>> node = firstAndRest.value();
        if (node == null)
        {
            throw new IllegalStateException("Sequence ist empty.");
        }
        return node.first();
    }

    @Override
    public TransformedSeq<M, N> rest()
    {
        Pair<N, TransformedSeq<M, N>> node = firstAndRest.value();
        if (node == null)
        {
            throw new IllegalStateException("Sequence ist empty.");
        }
        return node.second();
    }

    @Override
    public boolean isEmpty()
    {
        return firstAndRest.value() == null;
    }

    @Override
    public ForwardIterator<N> iterator()
    {
        return new IteratorFromSeq<N>(this);
    }
}