package org.gendut.collection;
import org.gendut.arithmetic.Int;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;


public abstract class AbstractCollection<E> implements ImmutableCollection<E>
{
    @Override
    public E first()
    {
        return iterator().next();
    }

    @Override
    public Seq<E> rest()
    {
        ForwardIterator<E> iterator = iterator();
        iterator.next();;
        return SeqFromIterator.create(iterator);
    }

    @Override
    public boolean isEmpty()
    {
        return elementCount() == Int.ZERO;
    }
    

	@Override
	public Stream<E> stream() {
		return Collections.stream(this);
	}
}
