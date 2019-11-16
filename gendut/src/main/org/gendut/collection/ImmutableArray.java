package org.gendut.collection;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;


// ! Immutable Array
/* <literate> */
/**
 * This class provides a fixed-size immutable array with update operation. Setting an element to a
 * value returns a new array.
 */
public final class ImmutableArray<E> extends ImmutableArrayBase<E>
                implements Array<E>
{
    private ImmutableArray(long sz)
    {
        super(sz);
    }

    @SuppressWarnings("rawtypes")
    static final private ImmutableArray emptyTuple = new ImmutableArray(
                    0);

    /**
     * Construct a new tuple of given size.
     */
    @SuppressWarnings("unchecked")
    static public <E> ImmutableArray<E> create(long size)
    {
        if (size == 0)
            return emptyTuple;
        else return new ImmutableArray<E>(size);
    }

    private ImmutableArray(ImmutableArray<E> x, long i, E e)
    {
        super(x, i, e);
    }

    public E at(long i)
    {
        return super.get(i);
    }

    @Override
    public E get(long i)
    {
        return super.get(i);
    }

    public E get(Long i)
    {
        return at(i);
    }

    /**
     * returns a tuple whith the <i>i</i>th element replaced.
     */
    public ImmutableArray<E> set(long i, E e)
    {
        return new ImmutableArray<E>(this, i, e);
    }

    public Seq<E> seq()
    {
        return SeqFromIterator.create(iterator());
    }

    public ForwardIterator<E> iterator()
    {
        return new Collections.RandomAccessIterator<E>(this);
    }

    @Override
    public Object[] asMutableArray()
    {
        return super.asMutableArray();
    }

    public boolean contains(E e)
    {
        return Collections.containsViaIterator(this, e);
    }

    public E find(E e)
    {
        return (Collections.findViaIterator(this, e));
    }

    public long size()
    {
        return super.adressableSize();
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
    public String toString()
    {
        return Collections.toStringIterationOrder(this);
    }

    /**
     * TODO: Optimize in order to achieve linear instead of logarithmic time.
     */
    static public <E> ImmutableArray<E> fromCollection(
                    ImmutableCollection<? extends E> C)
    {
        ImmutableArray<E> tuple = create(C.size());
        ForwardIterator<? extends E> it = C.iterator();
        long i = 0;
        while (it.hasNext())
        {
            tuple = tuple.set(i, it.next());
            i = i + 1;
        }
        return tuple;
    }

    public Int elementCount()
    {
        return Int.create(size());
    }
    

    @SuppressWarnings("unchecked")
    public ImmutableArray<E> clear()
    {
        return emptyTuple;
    }

    @Override
    public E first()
    {
        return seq().first();
    }

    @Override
    public Seq<E> rest()
    {
        return seq().rest();
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    public ImmutableArray<E> addAll(ImmutableCollection<E> rest)
    {
        ExtendibleArray<E> all = new ExtendibleArray<E>();
        all.addAll(this);
        all.addAll(rest);
        return fromCollection(all.asConstant());
    }
    

	@Override
	public Stream<E> stream() {
		return Collections.stream(this);
	}
}// `class`
