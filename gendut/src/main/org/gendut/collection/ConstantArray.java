package org.gendut.collection;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;


public final class ConstantArray<E> extends AbstractList<E> implements Array<E>
{
    private final int size;

    private static final int unitializedHashcode = -1;

    private volatile int hashValue = unitializedHashcode;

    private final Object data;

    private ConstantArray(Object array)
    {
        if (array.getClass() == Object[].class)
            array = normalizeData((Object[]) array);
        this.data = array;
        this.size = determineSize(array);
    }

    public static ConstantArray<Byte> fromArray(byte[] array)
    {
        return new ConstantArray<Byte>(array.clone());
    }

    public static ConstantArray<Integer> fromArray(int[] array)
    {
        return new ConstantArray<Integer>(array.clone());
    }

    static public <E> ConstantArray<E> fromArray(E[] array)
    {
        return new ConstantArray<E>(array.clone());
    }

    static public <E> ConstantArray<E> fromCollection(
                    ImmutableCollection<E> collection)
    {
        int n = (int) collection.size();
        Object[] array = new Object[n];
        ForwardIterator<E> it = collection.iterator();
        for (int i = 0; i < n; i++)
            array[i] = it.next();
        return new ConstantArray<E>(array);
    }

    static public <E> ConstantArray<E> fromSequence(Seq<E> collection)
    {
        ExtendibleArray<E> list = new ExtendibleArray<E>();
        ForwardIterator<E> it = collection.iterator();
        while (it.hasNext())
            list.add(it.next());
        return list.asConstant();
    }

    static public <E> ConstantArray<E> pair(E a, E b)
    {
        Object[] array = new Object[2];
        array[0] = a;
        array[1] = b;
        return new ConstantArray<E>(array);
    }

    static public ConstantArray<Byte> fromString(String text)
    {
        return new ConstantArray<Byte>(text.getBytes());
    }

    static public ConstantArray<Byte> fromString(String text, String encoding)
                    throws UnsupportedEncodingException
    {
        return new ConstantArray<Byte>(text.getBytes(encoding));
    }

    static public byte[] bytes(ConstantArray<Byte> array)
    {
        if (array.size() == 0)
            return new byte[0];
        else return ((byte[]) array.data).clone();
    }

    public static String asString(ConstantArray<Byte> array)
    {
        return new String(bytes(array));
    }

    public static String asString(ConstantArray<Byte> array, String encoding)
                    throws UnsupportedEncodingException
    {
        return new String(bytes(array), encoding);
    }

    static private Object normalizeData(Object[] data)
    {
        if (data.length == 0 || !allNonNullAndSameClasses(data))
            return data;
        Class<? extends Object> class1 = data[0].getClass();
        if (class1 == Byte.class)
            return toBytes(data);
        if (class1 == Integer.class)
            return toInts(data);
        else return data;
    }

    private static byte[] toBytes(Object[] data)
    {
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            bytes[i] = (Byte) data[i];
        return bytes;
    }

    private static int[] toInts(Object[] data)
    {
        int[] numbers = new int[data.length];
        for (int i = 0; i < data.length; i++)
            numbers[i] = (Integer) data[i];
        return numbers;
    }

    private static boolean allNonNullAndSameClasses(Object[] data)
    {
        if (data.length == 0)
            return true;
        if (data[0] == null)
            return false;
        Class<? extends Object> class1 = data[0].getClass();
        for (int i = 1; i < data.length; i++)
        {
            if (data[i] == null || data[i].getClass() != class1)
                return false;
        }
        return true;
    }

    @Override
    public long size()
    {
        return size;
    }

    private static int determineSize(Object data)
    {
        if (Object[].class.isInstance(data))
            return ((Object[]) data).length;
        else if (data.getClass() == byte[].class)
            return ((byte[]) data).length;
        else if (data.getClass() == int[].class)
            return ((int[]) data).length;
        else throw new IllegalStateException("Data has unexpected type: "
                        + data.getClass());
    }

    @Override
    public Int elementCount()
    {
        return Int.create(size());
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
    public ForwardIterator<E> iterator()
    {
        return new Collections.RandomAccessIterator<E>(this);
    }

    @Override
    public int hashCode()
    {
        if (hashValue == unitializedHashcode)
        {
            int hc = 0;
            if (data.getClass() == byte[].class)
            {
                byte[] array = (byte[]) data;
                for (int i = 0; i < array.length; i++)
                {
                    hc = hc + array[i] * 83;
                }
            }
            else if (data.getClass() == int[].class)
            {
                int[] array = (int[]) data;
                for (int i = 0; i < array.length; i++)
                {
                    hc = hc + (int) (array[i] * 83);
                }
            }
            else if (data.getClass() == long[].class)
            {
                long[] array = (long[]) data;
                for (int i = 0; i < array.length; i++)
                {
                    hc = hc + (int) (array[i] * 83);
                }
            }
            else hc = Collections.hashCodeForLists(this);
            if (hc == unitializedHashcode)
                hc = -17;
            return hc;
        }
        return hashValue;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || obj.hashCode() != this.hashCode())
            return false;
        return Collections.equalsForList(this, obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(long i)
    {
        if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException("Index " + i);
        int pos = (int) i;
        if (Object[].class == data.getClass())
            return (E) ((Object[]) data)[pos];
        else if (Object[].class.isInstance(data))
            return (E) ((Object[]) data)[pos];
        else if (data.getClass() == byte[].class)
            return (E) (Byte) (((byte[]) data)[pos]);
        else if (data.getClass() == int[].class)
            return (E) (Integer) (((int[]) data)[pos]);
        else if (Object[].class.isInstance(data))
            return (E) ((Object[]) data)[pos];
        else throw new IllegalStateException("Data has unexpected type: "
                        + data.getClass());
    }

    public Object[] asMutableArray()
    {
        int n = (int) size();
        Object[] array = new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = get(i);
        return array;
    }

    public ConstantArray<E> sort(Comparator<? super E> cmp)
    {
        if (size() <= 1)
            return this;
        Object[] arr = this.asMutableArray();
        sort(arr, cmp);
        return new ConstantArray<E>(arr);
    }

    static <E> void sort(Object[] a, Comparator<? super E> cmp)
    {
        if (a.length <= 1)
            return;
        if (a.length < 70)
            insertionSort(a, cmp);
        int n = a.length;
        int p = n / 2;
        Object[] left = new Object[p];
        for (int i = 0; i < p; i++)
            left[i] = a[i];
        Object[] right = new Object[n - p];
        for (int i = p; i < n; i++)
            right[i - p] = a[i];
        sort(left, cmp);
        sort(right, cmp);
        mergeInto(a, left, right, cmp);
    }

    @SuppressWarnings("unchecked")
    static <E> void insertionSort(Object[] a, Comparator<? super E> cmp)
    {
        int n = a.length;
        if (n <= 1)
            return;
        for (int k = 1; k < n; k++)
        {
            E x = (E) a[k];
            int i;
            for (i = k - 1; i >= 0; i--)
            {
                if (cmp.compare((E) a[i], x) > 0)
                {
                    a[i + 1] = a[i];
                }
                else
                {
                    a[i + 1] = x;
                    i = -99;
                    break;
                }
            }
            if (i != -99)
                a[0] = x;
        }
    }

    @SuppressWarnings("unchecked")
    static <E> void mergeInto(Object[] target, Object[] left, Object[] right,
                    Comparator<? super E> cmp)
    {
        if (target.length != left.length + right.length)
        {
            throw new IllegalArgumentException(
                            "Internal Error: Array lengths do not match!");
        }
        int p1 = 0;
        int p2 = 0;
        int pt = 0;
        while ((p1 < left.length) && (p2 < right.length))
        {
            if (cmp.compare((E) left[p1], (E) right[p2]) <= 0)
                target[pt++] = left[p1++];
            else target[pt++] = right[p2++];
        }
        if (p1 >= left.length)
        {
            left = right;
            p1 = p2;
        }
        while (p1 < left.length)
            target[pt++] = left[p1++];
    }
}
