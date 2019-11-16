package org.gendut.seq;

import org.gendut.collection.AbstractList;
import org.gendut.func.Function;
import org.gendut.func.LazyValue;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IteratorFromSeq;


// !Sequence Wrapper for Iterators
/* <literate> */
/**
 * !Sequence wrapper for iterators.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class SeqFromIterator<E> extends AbstractList<E> implements Seq<E>
{
    private SeqFromIterator()
    {
        firstValue = null;
        restSeq = null;
    };

    private static SeqFromIterator empty = new SeqFromIterator();

    public boolean isEmpty()
    {
        return this == empty;
    }

    public static <E> SeqFromIterator<E> create(
                    ForwardIterator<E> it)
    {
        if (!it.hasNext())
            return empty;
        else return new SeqFromIterator(it.next(), it);
    }

    private static Function<ForwardIterator, SeqFromIterator> restCons =
                    new Function<ForwardIterator, SeqFromIterator>()
                    {
                        public SeqFromIterator get(ForwardIterator it)
                        {
                            return create(it);
                        }
                    };

    private final E firstValue;

    private final LazyValue<SeqFromIterator> restSeq;

    private SeqFromIterator(E first, ForwardIterator<E> it)
    {
        firstValue = first;
        restSeq = new LazyValue<SeqFromIterator>(
                        restCons, it);
    }

    public E first()
    {
        if (isEmpty())
            throw new IllegalArgumentException("Sequence is empty");
        return (E) firstValue;
    }

    public Seq<E> rest()
    {
        if (isEmpty())
            throw new IllegalArgumentException("Sequence is empty");
        return restSeq.value();
    }

    @Override
    public ForwardIterator<E> iterator()
    {
        return new IteratorFromSeq<E>(this);
    }
}// `class`
