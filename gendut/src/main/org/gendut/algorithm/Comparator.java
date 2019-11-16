package org.gendut.algorithm;

import org.gendut.arithmetic.Rational;


public interface Comparator<E>
{
    public int compare(E a, E b);

    public static final Comparator<String> StringCaseInsentiveLexicographic = new Comparator<String>()
    {
        public int compare(String a, String b)
        {
            return a.compareToIgnoreCase(b);
        }
    };

    final static public Comparator<String> StringLexicographic = new Comparator<String>()
    {
        public int compare(String a, String b)
        {
            return a.compareTo(b);
        }
    };

    final static public Comparator<Rational> RationalNatural = new Comparator<Rational>()
    {
        public int compare(Rational a, Rational b)
        {
            return a.compareTo(b);
        }
    };

    final static public Comparator<Byte> ByteNatural = new Comparator<Byte>()
    {
        public int compare(Byte a, Byte b)
        {
            return a.compareTo(b);
        }
    };

    public static final Comparator<Long> LongNatural = new Comparator<Long>()
    {
        @Override
        public int compare(Long a, Long b)
        {
            return a.compareTo(b);
        }
    };

    public static final Comparator<Object> AllEquals = new Comparator<Object>()
    {
        @Override
        public int compare(Object a, Object b)
        {
            return 0;
        }
    };
}
