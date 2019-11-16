package org.gendut.collection.mutable;

import org.gendut.arithmetic.Int;
import org.gendut.collection.Collections;
import org.gendut.collection.ConstantArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IterableCollection;
import org.gendut.seq.Seq;


// !Bothside Extendible Array.
/**
 * Implements a bothside extendible array. This is an array in which element access takes time O(1),
 * as well as adding and removing new elements at first/last position.
 * 
 * Memory requirement can be up to 5 additional (references to) items per item, but is better on
 * average. It is also relatively easy to avoid this memory overhead by another level of indirection
 * (using buckets).
 */
@SuppressWarnings("unchecked")
public final class ExtendibleArray<E> implements IterableCollection<E>
{
    // N is the size of the small array A
    // NTwice is the size of the large array B
    private int NTwice, N;

    private Object A[], B[];

    // The masks for quickly computing the remainders
    // of N and NTwice, respectively
    private int maskN, maskNTwice;

    // the indizes for maintaining the queues:
    private int p0, q0, p1, q1, p2, q2;

    // size of the first queue (this is the one in B!)
    private int b;

    // the initial capacity ( must be a power of 2):
    private static final int INITIAL_CAPACITY = 32;

    // ========== Debugging and Memory Management : ==========
    /*
     * The implementation maintains two arrays, A and B, which are used as round buffers. B has
     * always twice the size of A. The queue is splitted into three sequences: the first part is in
     * B, the second in A, the third again in B exactly opposite to the first part. This scheme
     * allows us to copy no more than one element per update.
     */
    // debug output of internal structure:
    public String debug()
    {
        StringBuffer s = new StringBuffer();
        s.append("Size=" + size());
        s.append("  p0=" + p0);
        s.append("  q0=" + q0);
        s.append("  p1=" + p1);
        s.append("  p1=" + q1);
        s.append("  p2=" + p2);
        s.append("  q2=" + q2);
        s.append("  b=" + b);
        s.append("Array A=");
        printSubarray(A, s);
        s.append("Array B=");
        printSubarray(B, s);
        return s.toString();
    }

    private void printSubarray(Object[] A, StringBuffer s)
    {
        if (A != null)
        {
            s.append("(");
            for (int i = 0; i < A.length; i++)
            {
                if (i > 0)
                {
                    s.append(",");
                }
                if (A[i] != null)
                {
                    s.append(A[i].toString());
                }
                else
                {
                    s.append("*");
                }
            }
            s.append(")");
        }
        else
        {
            s.append("*");
        }
    }

    // ========== Initialization and Construction : ==========
    private void init()
    {
        NTwice = INITIAL_CAPACITY;
        N = NTwice / 2;
        maskN = N - 1;
        maskNTwice = NTwice - 1;
        // if initialCapacity==NTwice then the small array 0
        // will always be non-existent.
        A = null;
        B = new Object[NTwice];
        b = 0;
        p1 = q1 = p0 = q0 = p2 = q2 = 0;
    }

    public ExtendibleArray()
    {
        init();
    }

    public ExtendibleArray(int size)
    {
        init();
        for (int i = 0; i < size; i++)
        {
            addLast(null);
        }
    }

    public final long size()
    {
        if (NTwice == INITIAL_CAPACITY)
        {
            return b;
        }
        else
        {
            return b + N;
        }
    }

    public final E get(long k)
    {
        if ((k < 0) || (k >= size()))
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int i = (int) k;
        if ((i < b) || (i >= N))
        {
            return (E) B[(i + p0) & maskNTwice];
        }
        else
        {
            return (E) A[(i + p1 - b) & maskN];
        }
    }

    public final E last()
    {
        return get(size() - 1);
    }

    public final E first()
    {
        return get(0);
    }

    public final E set(int i, E x)
    {
        if ((i < 0) || (i >= size()))
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if ((i < b) || (i >= N))
        {
            int k = (i + p0) & maskNTwice;
            Object old = B[k];
            B[k] = x;
            return (E) old;
        }
        else
        {
            int k = (i + p1 - b) & maskN;
            Object old = A[k];
            A[k] = x;
            return (E) old;
        }
    }

    public final void addFirst(E x)
    {
        if (NTwice != INITIAL_CAPACITY)
        { // move one item from A to B
            q1--;
            q1 &= maskN;
            p2--;
            p2 &= maskNTwice;
            B[p2] = A[q1];
            A[q1] = null;
        }
        // add item at front:
        p0--;
        p0 &= maskNTwice;
        B[p0] = x;
        b++;
        if (p0 == q2)
        {
            makeNewB(); // make B to A and create empty B
        }
    }

    public final void addLast(E x)
    {
        if (NTwice != INITIAL_CAPACITY)
        { // move one item from A to B
            B[q0++] = A[p1];
            A[p1++] = null;
            q0 &= maskNTwice;
            p1 &= maskN;
        }
        // add item at tail:
        B[q2++] = x;
        q2 &= maskNTwice;
        b++;
        if (p0 == q2)
        {
            makeNewB(); // make B to A and create empty B
        }
    }

    public final boolean add(E x)
    {
        addLast(x);
        return true;
    }

    private void makeNewB()
    {
        N = NTwice;
        NTwice = 2 * NTwice;
        maskN = N - 1;
        maskNTwice = NTwice - 1;
        A = B;
        B = new Object[NTwice];
        b = 0;
        p1 = q1 = p0;
        p0 = q0 = 0;
        p2 = q2 = N;
    }

    public final E removeFirst()
    {
        if (size() <= 0)
        {
            throw new java.util.NoSuchElementException();
        }
        if (NTwice != INITIAL_CAPACITY)
        {
            if (b == 0)
            {
                // B is empty => A is full
                makeNewA(); // make A to B and create empty A
            }
            if (NTwice != INITIAL_CAPACITY)
            { // move one item from B to A
                A[q1++] = B[p2];
                B[p2++] = null;
                q1 &= maskN;
                p2 &= maskNTwice;
            }
            // remove item from front
            b--;
            Object x = B[p0];
            B[p0++] = null;
            p0 &= maskNTwice;
            return (E) x;
        }
        else if (p0 != q2)
        { // remove item from front
            b--;
            Object x = B[p0];
            B[p0++] = null;
            p0 &= maskNTwice;
            return (E) x;
        }
        else
        {
            return null;
        }
    }

    public final E removeLast()
    {
        if (size() <= 0)
        {
            throw new java.util.NoSuchElementException();
        }
        if (NTwice != INITIAL_CAPACITY)
        {
            if (b == 0)
            {
                // B is empty => A is full
                makeNewA(); // make A to B and create empty A
            }
            if (NTwice != INITIAL_CAPACITY)
            { // move one item from B to A
                p1--;
                p1 &= maskN;
                q0--;
                q0 &= maskNTwice;
                A[p1] = B[q0];
                B[q0] = null;
            }
            // remove item from tail
            b--;
            q2--;
            q2 &= maskNTwice;
            Object x = B[q2];
            B[q2] = null;
            return (E) x;
        }
        else if (p0 != q2)
        { // remove item from tail
            b--;
            q2--;
            q2 &= maskNTwice;
            Object x = B[q2];
            B[q2] = null;
            return (E) x;
        }
        else
        {
            return null;
        }
    }

    private void makeNewA()
    { // make A to B and create empty A
        NTwice = N;
        N = N / 2;
        maskN = N - 1;
        maskNTwice = NTwice - 1;
        B = A;
        p0 = q2 = p1;
        q0 = p2 = (p1 + N) % NTwice;
        p1 = q1 = 0;
        if (NTwice != INITIAL_CAPACITY)
        {
            A = new Object[N];
            b = N;
        }
        else
        {
            A = null;
            b = NTwice;
        }
    }

    // copy to standard array:
    public final Object[] toArray()
    {
        int n = (int) this.size();
        Object[] A = new Object[n];
        for (int i = 0; i < n; i++)
        {
            A[i] = get(i);
        }
        return A;
    }

    public final E[] toArray(E A[])
    {
        if (A.length != size())
            throw new IllegalArgumentException("Array " + "has wrong size "
                            + A.length + ", expected: " + size());
        int n = (int) this.size();
        for (int i = 0; i < n; i++)
        {
            A[i] = get(i);
        }
        return A;
    }

    public final void insert(int i, E x)
    {
        if (i == size())
        {
            add(x);
            return;
        }
        int n = (int) size() - 1;
        Object tmp = get(n);
        addLast((E) tmp);
        for (int j = n; j > i; j--)
        {
            set(j, get(j - 1));
        }
        set(i, x);
    }

    public final E pop()
    {
        return removeLast();
    }

    public final E top()
    {
        return get(size() - 1);
    }

    public final void push(E item)
    {
        addLast(item);
    }

    public Int elementCount()
    {
        return Int.create(size());
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public void addAll(Seq<? extends E> other)
    {
        ForwardIterator<? extends E> it = other.iterator();
        while (it.hasNext())
            add(it.next());
    }

    public void clear()
    {
        while (!isEmpty())
            removeLast();
    }

    public void add(int k, E v)
    {
        if (k < 0 || k > size())
            throw new IndexOutOfBoundsException("" + k);
        if (k == size())
        {
            add(v);
            return;
        }
        else
        {
            add(null);
            for (int i = (int) size() - 1; i > k; i--)
            {
                set(i, get(i - 1));
            }
        }
        set(k, v);
    }
    
    public E remove(int i) {
		E result = get(i);
		for (int k = i+1; k < size(); k++) {
			set(k - 1, get(k));
		}
		removeLast();
		return result;
	}

    public static <E> ExtendibleArray<E> fromArray(E[] a)
    {
        ExtendibleArray<E> result = new ExtendibleArray<E>();
        for (int i = 0; i < a.length; i++)
            result.add(a[i]);
        return result;
    }

    public ConstantArray<E> asConstant()
    {
        int n = (int) size();
        E[] array = (E[]) new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = get(i);
        return ConstantArray.fromArray(array);
    }

    @Override
    final public String toString()
    {
        return asConstant().toString();
    }

    @Override
    public ForwardIterator<E> iterator()
    {
        return asConstant().iterator();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null || other.getClass() != ExtendibleArray.class)
            return false;
        ExtendibleArray<?> otherArray = (ExtendibleArray<?>) other;
        if (size() != otherArray.size())
            return false;
        long n = size();
        for (long i = 0; i < n; i++)
            if (!get(i).equals(otherArray.get(i)))
                return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        return Collections.hashCodeForLists(this);
    }
}// `class`
