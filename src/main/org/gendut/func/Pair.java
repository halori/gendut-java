package org.gendut.func;

import java.util.Comparator;

public final class Pair<A, B>
{
    private final A first;

    private final B second;

    public Pair(A first, B second)
    {
        super();
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        @SuppressWarnings("rawtypes") Pair other = (Pair) obj;
        if (first == null)
        {
            if (other.first != null) return false;
        }
        else if (!first.equals(other.first)) return false;
        if (second == null)
        {
            if (other.second != null) return false;
        }
        else if (!second.equals(other.second)) return false;
        return true;
    }

    public A first()
    {
        return first;
    }

    public B second()
    {
        return second;
    }

    public static <A, B> Pair<A, B> create(A first, B second)
    {
        return new Pair<A, B>(first, second);
    }

    @Override
    public String toString()
    {
        StringBuffer text = new StringBuffer();
        text.append('(');
        Pair<?, ?> node = this;
        while (node != null && node instanceof Pair)
        {
            if (node != this)
            {
                text.append(" ");
            }
            String elementAsString = node.first == null ? "<null>" : node.first.toString();
            text.append(elementAsString);
            if (node.second == null || !(node.second instanceof Pair))
            {
                text.append(" . ");
                text.append(""+node.second);
                node = null;
            }
            else
            {
                node = (Pair<?, ?>) node.second;
            }
        }
        text.append(")");
        return text.toString();
    }

    public static <U, V> Comparator<Pair<U, V>> naturalOrder(final Comparator<? super U> firstOrder,
                    final Comparator<? super V> secondOrder)
    {
        return new Comparator<Pair<U, V>>()
        {
            @Override
            public int compare(Pair<U, V> a, Pair<U, V> b)
            {
                int cmp = firstOrder.compare(a.first, b.first);
                if (cmp != 0)
                    return cmp;
                else return secondOrder.compare(a.second, b.second);
            }
        };
    }
}// `class`
