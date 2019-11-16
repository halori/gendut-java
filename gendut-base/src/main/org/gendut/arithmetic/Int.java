package org.gendut.arithmetic;

import java.util.Random;

//! Integers
/*<literate>*/
/**
It is discouraged to specialize this class outside from its package.
**/
public abstract class Int extends Rational {

  /**
   * Users cannot call the default constructor.
   */
  protected Int() {}
  
  public static final Int MINUS_ONE = LargeIntegerNumberImpl.create(-1);  
  public static final Int ZERO = LargeIntegerNumberImpl.create(0);
  public static final Int ONE = LargeIntegerNumberImpl.create(1);
  public static final Int TWO = LargeIntegerNumberImpl.create(2);
  public static final Int THREE = LargeIntegerNumberImpl.create(3);
  public static final Int FOUR = LargeIntegerNumberImpl.create(4);
  public static final Int FIVE = LargeIntegerNumberImpl.create(5);
  public static final Int TEN = LargeIntegerNumberImpl.create(10);
  
  
    public static Int create(long value) {
      return LargeIntegerNumberImpl.create(value);
    }
    
    public static Int valueOf(long value) {
      return LargeIntegerNumberImpl.create(value);
    }
    
    public static Int valueOf(String value) {
      return create(new LargeInteger(value));
    }
    
    public static Int valueOf(String value, int radix) {
      return create(new LargeInteger(value, radix));
    }
  
    static Int create(LargeInteger value) {
      return LargeIntegerNumberImpl.create(value);
    }
    
    @Override
    public boolean isInteger() { return true; }
    
    @Override
    public Int Nominator() {
      return this;
    }
    
    @Override
    public Int Denominator() {
      return ONE;
    }
    
    public static Int createFromByteArray(byte[] bytes) {
      LargeInteger x = new LargeInteger(bytes);
      long asLong = x.longValue();
      if (LargeInteger.valueOf(asLong).equals(x))
        return create(asLong);
      else return new LargeIntegerNumberImpl(x);  
    }
    
    /**
     * returns a faithful representation as a byte array.
     */
    public abstract byte[] toByteArray();
    
    @Override
    public abstract Int add(Int b);
    protected abstract Int add(WordIntegerNumberImpl b);
    protected abstract Int add(LargeIntegerNumberImpl b);
    @Override
    public abstract Int multiply(Int b);
    protected abstract Int multiply(WordIntegerNumberImpl b);
    protected abstract Int multiply(LargeIntegerNumberImpl b);
    public abstract Int subtract(Int b);
    protected abstract Int subtractFrom(WordIntegerNumberImpl  b);
    protected abstract Int subtractFrom(LargeIntegerNumberImpl b);
    @Override
    public abstract int compareTo(Int b);
    protected abstract int compareTo(WordIntegerNumberImpl b);
    protected abstract int compareTo(LargeIntegerNumberImpl b);
    public abstract Int gcd(Int b);
    protected abstract Int gcd(WordIntegerNumberImpl b);
    protected abstract Int gcd(LargeIntegerNumberImpl b);
    public abstract Int intDiv(Int b);
    protected abstract Int intDivFrom(WordIntegerNumberImpl b);
    protected abstract Int intDivFrom(LargeIntegerNumberImpl b);
 
    public abstract int intValue();
    public abstract long longValue();
           
    @Override
    public Int add(long b)
    {return add(Rational.create(b));  }
    @Override
    public Int multiply(long b)
    {return multiply(Rational.create(b));  }
    @Override
    public Int subtract(long b)
    {return subtract(Rational.create(b));  }
    @Override
    public int compareTo(long b)
    {return compareTo(Rational.create(b));  }
    
    static public Int probablePrime(int bitLength, Random rnd) {
      return create(LargeInteger.probablePrime(bitLength, rnd));
    }
    
    abstract LargeInteger asLargeInteger();
    
    public Int pow(int exp) {
      return create(asLargeInteger().pow(exp));
    }
    
    public Int shiftLeft(int n) {
      return create(asLargeInteger().shiftLeft(n));
    }
    
    public Int shiftRight(int n) {
      return create(asLargeInteger().shiftRight(n));
    }
    
    public Int negate() {
      return create(asLargeInteger().negate());
    }
    
    public Int not() {
      return create(asLargeInteger().not());
    }
    
    public Int abs() {
      return create(asLargeInteger().abs());
    }
    
    public int bitLength() {
      return asLargeInteger().bitLength();
    }
    
    public double doubleValue() {
      return asLargeInteger().doubleValue();
    }
    
    public String toString(int radix) {
      return asLargeInteger().toString(radix);
    }
    
    public Int divideInt(Int divisor) {
      return create(asLargeInteger().divide(divisor.asLargeInteger()));
    }
   
    public Int mod(Int divisor) {
      return create(asLargeInteger().mod(divisor.asLargeInteger()));
    }
   
    
    public Int xor(Int other) {
      return create(asLargeInteger().xor(other.asLargeInteger()));
    }
    
    public Int and(Int other) {
      return create(asLargeInteger().and(other.asLargeInteger()));
    }
    
    public Int or(Int other) {
      return create(asLargeInteger().or(other.asLargeInteger()));
    }
    
    public boolean testBit(int n) {
      return asLargeInteger().testBit(n);
    }
    
    
}
