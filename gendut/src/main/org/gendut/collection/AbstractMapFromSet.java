package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.func.Pair;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;


/**
 * This class is for internal purpose only. When specializing from this class and the underlying set
 * calls a.equals(b) in order to locate some element, the left-hand side a must always(!) be an
 * element contained in the set. This leaves the right-hand side b to be the key which is searched
 * for. The reason is that while the map stores map entries into the set, it does not encapsulate
 * the search key in a map entry.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
abstract class AbstractMapFromSet<Key, Value, ReturnType extends AbstractMapFromSet<Key, Value, ReturnType>>
                extends AbstractCollection<Pair<Key, Value>>
                implements UpdatableMap<Key, Value, ReturnType>
{
    protected static final class MapEntry<Key, Value>
    {
        final Key key;

        final Value value;

        public MapEntry(final Key key, final Value value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public int hashCode()
        {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if ((obj != null) && (obj.getClass() == MapEntry.class))
                return key.equals(((MapEntry) obj).key);
            return key.equals(obj);
        }

        public static Comparator keyComparator(final Comparator cmp)
        {
            return new Comparator()
            {
                public int compare(Object a, Object b)
                {
                    if (a != null && a.getClass() == MapEntry.class)
                        a = ((MapEntry) a).key;
                    if (b != null && b.getClass() == MapEntry.class)
                        b = ((MapEntry) b).key;
                    return cmp.compare(a, b);
                }
            };
        }
    }

    protected final UpdatableSet<Object, ?> asSet;

    protected AbstractMapFromSet(UpdatableSet<Object, ?> set)
    {
        asSet = set;
    }

    final public boolean containsKey(Key key)
    {
        return asSet.contains(key);
    }

    private final class KeyIterator implements ForwardIterator<Key>
    {
        final ForwardIterator<Object> setIter;

        public KeyIterator(final ForwardIterator<Object> setIter)
        {
            this.setIter = setIter;
        }

        public boolean hasNext()
        {
            return setIter.hasNext();
        }

        public Key next()
        {
            return ((MapEntry<Key, Value>) setIter.next()).key;
        }
    }

    final public ForwardIterator<Key> keyIterator()
    {
        return new KeyIterator(asSet.iterator());
    }

    final public Seq<Key> keySeq()
    {
        return SeqFromIterator.create(keyIterator());
    }

    final public Value get(Key key)
    {
        Object item = asSet.find(key);
        if (item == null)
            return null;
        else return ((MapEntry<Key, Value>) item).value;
    }

    final public boolean contains(Pair<Key, Value> e)
    {
        Key k = e.first();
        if (k == null)
            return false;
        if (!containsKey(k))
            return false;
        Value v = get(k);
        if (v == null)
            return e.second() == null;
        else return v.equals(e.second());
    }

    private final class Iterator implements ForwardIterator<Pair<Key, Value>>
    {
        final ForwardIterator<Object> setIter;

        public Iterator(final ForwardIterator<Object> setIter)
        {
            this.setIter = setIter;
        }

        public boolean hasNext()
        {
            return setIter.hasNext();
        }

        public Pair<Key, Value> next()
        {
            MapEntry entry = (MapEntry<Key, Value>) setIter.next();
            return new Pair(entry.key, entry.value);
        }
    }

    final public ForwardIterator<Pair<Key, Value>> iterator()
    {
        return new Iterator(asSet.iterator());
    }

    final public Seq<Pair<Key, Value>> seq()
    {
        return SeqFromIterator.create(iterator());
    }

    final public long size()
    {
        return asSet.size();
    }

    @Override
    final public int hashCode()
    {
        return asSet.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return Collections.equalsForSets(this, obj);
    }

    @Override
    public String toString()
    {
        return Collections.toStringSorted(this);
    }

    public Pair<Key, Value> find(Pair<Key, Value> e)
    {
        Key k = e.first();
        if (k == null)
            return null;
        MapEntry<Key, Value> entry = (MapEntry<Key, Value>) asSet.find(e);
        if (entry == null)
            return null;
        Pair<Key, Value> candidate = new Pair<Key, Value>(entry.key, entry.value);
        if (candidate.equals(e))
            return candidate;
        else return null;
    }

    private class KeySet extends AbstractCollection<Key> implements ImmutableSet<Key>
    {
        public Key find(Key key)
        {
            return findKey(key);
        }

        public boolean contains(Key key)
        {
            return containsKey(key);
        }

        public ForwardIterator<Key> iterator()
        {
            return keyIterator();
        }

        public long size()
        {
            return asSet.size();
        }

        @Override
        public String toString()
        {
            return Collections.toStringSorted(this);
        }

        @Override
        public int hashCode()
        {
            return Collections.hashCodeForSet(this);
        }

        @Override
        public boolean equals(Object obj)
        {
            return Collections.equalsForSets(this, obj);
        }

        public Int elementCount()
        {
            return Int.create(size());
        }
    }

    public ImmutableSet<Key> keys()
    {
        return new KeySet();
    }

    public Key findKey(Key key)
    {
        MapEntry<Key, Value> entry = (MapEntry<Key, Value>) asSet.find(key);
        if (entry == null)
            return null;
        else return entry.key;
    }

    public Int elementCount()
    {
        return Int.create(size());
    }
}
