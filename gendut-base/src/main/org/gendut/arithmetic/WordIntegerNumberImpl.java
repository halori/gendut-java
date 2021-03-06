package org.gendut.arithmetic;


/**
 * Representation of word-size integers
 **/
final class WordIntegerNumberImpl extends Int {
    private final int value;

    static final long MIN_VALUE_AS_LONG = -2000000;

    static final long MAX_VALUE_AS_LONG = 2000000;

    static final LargeInteger MIN_VALUE = LargeInteger.valueOf(MIN_VALUE_AS_LONG);

    static final LargeInteger MAX_VALUE = LargeInteger.valueOf(MAX_VALUE_AS_LONG);

    private final static int MIN_SINGLETON = -200;   
    
    private final static int MAX_SINGLETON = 800;
    
    private final static int HALF_MAX_SINGLETON = MAX_SINGLETON / 2;

    private final static WordIntegerNumberImpl[] positive_singletons;

    private final static WordIntegerNumberImpl[] negative_singletons;

    static {
        positive_singletons = new WordIntegerNumberImpl[MAX_SINGLETON + 1];
        for (int i = 0; i <= MAX_SINGLETON; i++) {
            positive_singletons[i] = new WordIntegerNumberImpl(i);
        }
        negative_singletons = new WordIntegerNumberImpl[-MIN_SINGLETON + 1];
        for (int i = MIN_SINGLETON; i < 0; i++) {
            negative_singletons[-i] = new WordIntegerNumberImpl(i);
        }
    }

    private WordIntegerNumberImpl(int a) {
        value = a;
    }

    protected Integer getValue() {
        return value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public byte[] toByteArray() {
        return LargeInteger.valueOf(value).toByteArray();
    }

    public static Int create(int a) {
        if ((0 <= a) && (a <= MAX_SINGLETON)) {
            return positive_singletons[a];
        }
        if ((MIN_SINGLETON <= a) && (a < 0)) {
            return negative_singletons[-a];
        }
        return new WordIntegerNumberImpl(a);
    }

    public static Int create(long a) {
        /*
         * This method can only be accessed from within this package. We assert
         * that <i> a </i> is within bounds.
         */
        if (!((MIN_VALUE_AS_LONG <= a) && (a <= MAX_VALUE_AS_LONG))) {
            throw new IllegalStateException("Programming error: tried to " + "instantiate the large number " + a
                + " as word.");
        }
        if ((0 <= a) && (a <= MAX_SINGLETON)) {
            return positive_singletons[(int) a];
        }
        if ((MIN_SINGLETON <= a) && (a < 0)) {
            return negative_singletons[-((int) a)];
        }
        return new WordIntegerNumberImpl((int) a);
    }

    // double dispatch:
    @Override
    public Rational add(Rational b) {
        if (this == Int.ZERO) {
            return b;
        }
        if (b == Int.ZERO) {
            return this;
        }
        return b.add(this);
    }

    @Override
    public Rational multiply(Rational b) {
        if (this == Int.ZERO) {
            return b;
        }
        if (b == Int.ZERO) {
            return this;
        }
        return b.multiply(this);
    }

    @Override
    public Rational subtract(Rational b) {
        if (b == Int.ZERO) {
            return this;
        }
        return b.subtractFrom(this);
    }

    @Override
    public int compareTo(Rational b) {
        if (this == b) {
            return 0;
        }
        return -b.compareTo(this);
    }

    @Override
    public Int add(Int b) {
        if (b.getClass() == WordIntegerNumberImpl.class) {
            int bvalue = ((WordIntegerNumberImpl)b).value;
            if (value >= 0 && value < HALF_MAX_SINGLETON && bvalue >= 0 && bvalue < HALF_MAX_SINGLETON)
                return positive_singletons[value+bvalue];
        }
        return b.add(this);
    }

    @Override
    public Int multiply(Int b) {
        if (this == Int.ZERO) {
            return ZERO;
        }
        if (b == Int.ZERO) {
            return ZERO;
        }
        return b.multiply(this);
    }

    @Override
    public Int subtract(Int b) {
        if (b == Int.ZERO) {
            return this;
        }
        return b.subtractFrom(this);
    }

    @Override
    public Int subtractFrom(Int b) {
        if (this == Int.ZERO) {
            return b;
        }
        return b.subtract(this);
    }

    @Override
    public int compareTo(Int b) {
        if (this == b) {
            return 0;
        }
        return -b.compareTo(this);
    }

    @Override
    public Int gcd(Int b) {
        return b.gcd(this);
    }

    @Override
    public Int add(WordIntegerNumberImpl b) {
        long c = (long) value + (long) b.value;
        if ((0 <= c) && (c <= MAX_SINGLETON)) {
            return positive_singletons[(int) c];
        }
        if ((MIN_SINGLETON <= c) && (c < 0)) {
            return negative_singletons[-((int) c)];
        }
        if ((MIN_VALUE_AS_LONG <= c) && (c <= MAX_VALUE_AS_LONG)) {
            return create(c);
        } else {
            return new LargeIntegerNumberImpl(LargeInteger.valueOf(c));
        }
    }

    @Override
    public Int add(LargeIntegerNumberImpl b) {
        return b.add(this);
    }

    @Override
    public Rational add(RationalNumberImpl b) {
        return b.add(this);
    }

    @Override
    public Int multiply(WordIntegerNumberImpl b) {
        long c = (long) value * (long) b.getValue();
        if ((MIN_VALUE_AS_LONG <= c) && (c <= MAX_VALUE_AS_LONG)) {
            return create(c);
        } else {
            return new LargeIntegerNumberImpl(LargeInteger.valueOf(c));
        }
    }

    @Override
    public Int multiply(LargeIntegerNumberImpl b) {
        return b.multiply(this);
    }

    @Override
    public Rational multiply(RationalNumberImpl b) {
        return b.multiply(this);
    }

    @Override
    public Int subtractFrom(WordIntegerNumberImpl b) {
        long c = (long) b.getValue() - (long) value;
        if ((MIN_VALUE_AS_LONG <= c) && (c <= MAX_VALUE_AS_LONG)) {
            return create(c);
        } else {
            return new LargeIntegerNumberImpl(LargeInteger.valueOf(c));
        }
    }

    @Override
    public Int subtractFrom(LargeIntegerNumberImpl b) {
        LargeInteger c = b.getValue().subtract(LargeInteger.valueOf(value));
        return LargeIntegerNumberImpl.create(c);
    }

    @Override
    public Rational subtractFrom(RationalNumberImpl b) {
        return b.subtract(this);
    }

    @Override
    public Rational inverseZ() {
        return Rational.create(1, value);
    }

    @Override
    public Rational inverse() {
        if (this == ZERO) {
            throw new ArithmeticException("Division by Zero");
        }
        return Rational.create(1, value);
    }

    @Override
    public int compareTo(WordIntegerNumberImpl b) {
        if (this == b) {
            return 0;
        }
        int bval = b.getValue();
        if (value < bval) {
            return -1;
        } else if (value == bval) {
            return 0;
        } else {
            return +1;
        }
    }

    @Override
    public int compareTo(LargeIntegerNumberImpl b) {
        if ((Object) this == b) {
            return 0;
        }
        return -b.compareTo(this);
    }

    @Override
    public int compareTo(RationalNumberImpl b) {
        if ((Object) this == b) {
            return 0;
        }
        return -b.compareTo(this);
    }

    @Override
    public Int gcd(WordIntegerNumberImpl B) {
        long a = value;
        long b = B.getValue();
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }

        if (a < b) {
            long tmp = a;
            a = b;
            b = tmp;
        }// if
        while (b > 0) {
            long c = a % b;
            a = b;
            b = c;
        }// while

        return create(a);

    }

    @Override
    public Int gcd(LargeIntegerNumberImpl b) {
        return b.gcd(this);
    }

    @Override
    public Int intDiv(Int b) {
        return b.intDivFrom(this);
    }

    @Override
    public Int intDivFrom(WordIntegerNumberImpl b) {
        return create(b.getValue() / value);
    }

    @Override
    public Int intDivFrom(LargeIntegerNumberImpl b) {
        return create(b.getValue().divide(LargeInteger.valueOf(value)));
    }

    @Override
    public int signum() {
        if (value < 0) {
            return -1;
        } else if (value > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String str = String.valueOf(value);
        if ((0 <= value) && (value <= MAX_SINGLETON)) {
            if (positive_singletons[value] != this) {
                return str + "REPORT ERROR: Word should be singleton!";
            } else {
                return str;
            }
        }
        if ((MIN_SINGLETON <= value) && (value < 0)) {
            if (negative_singletons[-(value)] != this) {
                return str + "REPORT ERROR: Word should be singleton!";
            } else {
                return str;
            }
        }
        return str;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    LargeInteger asLargeInteger() {
      return LargeInteger.valueOf(this.value);
    }
}// class IWord
