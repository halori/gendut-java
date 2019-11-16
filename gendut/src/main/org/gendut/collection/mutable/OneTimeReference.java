package org.gendut.collection.mutable;

/**
 * A one-time reference is used only once, when obtained, its value is nullified.
 */
public final class OneTimeReference<T>
{
    private volatile T value;

    public OneTimeReference(T value)
    {
        this.value = value;
    }

    public T get()
    {
        T v = value;
        value = null;
        return v;
    }
}
