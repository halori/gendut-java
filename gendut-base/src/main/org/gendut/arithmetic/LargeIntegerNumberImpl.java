package org.gendut.arithmetic;


//! Internal Representation
/*<literate>*/
/**
Representation of large Numbers
**/
final class LargeIntegerNumberImpl extends Int
{      
    private final LargeInteger value;
    
    protected LargeIntegerNumberImpl(LargeInteger a) {
      value = a;
    }
   
    static public Int create(LargeInteger a) {
        if ((a.compareTo(WordIntegerNumberImpl.MIN_VALUE)>=0) 
        && (a.compareTo(WordIntegerNumberImpl.MAX_VALUE)<=0))
          return WordIntegerNumberImpl.create(a.longValue());
        else 
          return new LargeIntegerNumberImpl(a);
    }
    
    static public Int create(long a) {
        if ((a >= WordIntegerNumberImpl.MIN_VALUE_AS_LONG) 
        && (a <= WordIntegerNumberImpl.MAX_VALUE_AS_LONG))
          return WordIntegerNumberImpl.create(a);
        else 
          return new LargeIntegerNumberImpl(LargeInteger.valueOf(a));
    }
    
    LargeInteger getValue() {
        return value;
    }
    
    @Override
    public int intValue() {
      return value.intValue();
    }
    
    @Override
    public long longValue() {
      return value.longValue();
    }
    
    //double dispatch:
    @Override
    public Rational add(Rational b) { 
        if (b == ZERO) return this;
        return b.add(this); 
    }
    @Override
    public Int add(Int b) { 
        if (b == ZERO) return this;
        return b.add(this); 
    }
    
    @Override
    public Rational multiply(Rational b) { 
        if (b == ONE) return this;
        return b.multiply(this); 
    }
    @Override
    public Int multiply(Int b) { 
        if (b == ONE) return this;
        return b.multiply(this); 
    }
    
      @Override
      public Rational subtract(Rational b) { 
        if (b == ZERO) return this;
        return b.subtractFrom(this); 
    }
    
    @Override
    public Int subtract(Int b) { 
          if (b == ZERO) return this;
          return b.subtractFrom(this); 
    }
    
    @Override
    public Int subtractFrom(Int b) { 
        if (this == ZERO) return b;
        return b.subtractFrom(this); 
  }
      
    @Override
    public int compareTo(Rational b) {
        return -b.compareTo(this);
    }
    
    @Override
    public int compareTo(Int b) {
        return -b.compareTo(this);
    }
       
    //implementation: 
    
    @Override
    public byte[] toByteArray() {
      return value.toByteArray();
    }
    
    @Override
    public Int add(WordIntegerNumberImpl b) {
      return create(value.add(LargeInteger.valueOf(b.getValue())));
    }
     
    @Override
    public Int add(LargeIntegerNumberImpl b) {
      return create(value.add(b.getValue()));
    }
  
    @Override
    public Rational add(RationalNumberImpl b) {
      return b.add(this);
    }
     
    @Override
    public Int multiply(WordIntegerNumberImpl b) {
        return create(value.multiply(LargeInteger.valueOf(b.getValue())));
      }
       
      @Override
      public Int multiply(LargeIntegerNumberImpl b) {
        return create(value.multiply(b.getValue()));
      }
    
      @Override
      public Rational multiply(RationalNumberImpl b) {
        return b.multiply(this);
      }
      
     @Override
    public Int subtractFrom(WordIntegerNumberImpl b) {
       return new LargeIntegerNumberImpl(
           LargeInteger.valueOf(b.getValue()).subtract(value));
    }
     
    @Override
    public Int subtractFrom(LargeIntegerNumberImpl b) {
       return new LargeIntegerNumberImpl(b.getValue().subtract(value));
    }
     
    @Override
    public Rational subtractFrom(RationalNumberImpl b) {
      return b.subtract(this);
    }
    
    @Override
    public Rational inverseZ()
    {
    	return Rational.createFraction(LargeInteger.ONE,value);
    }
    
    @Override
    public Rational inverse()
    {
      if (this == ZERO)
        throw new ArithmeticException("Division by Zero");
      return Rational.createFraction(LargeInteger.ONE,value);
    }
    
    @Override
    public int compareTo(WordIntegerNumberImpl b) {
      if ((Object) this == b)
        return 0;
      return value.compareTo(LargeInteger.valueOf(b.getValue()));
    }
    
    @Override
    public int compareTo(LargeIntegerNumberImpl b) {
      if (this == b)
        return 0;
      return value.compareTo(b.getValue());
    }
    
    @Override
    public int compareTo(RationalNumberImpl b) {
      if ((Object) this == b)
        return 0;
      return -b.compareTo(this);
    }
   
    @Override
    public Int gcd(Int b)
    { return b.gcd(this); }
    
    @Override
    public Int gcd(WordIntegerNumberImpl b) 
    { return create(value.gcd(LargeInteger.valueOf(b.getValue()))); }
    
    @Override
    public Int gcd(LargeIntegerNumberImpl b)
    { return create(value.gcd(b.getValue())); } 
    
    @Override
    public Int intDiv(Int b)
    { 
      return b.intDivFrom(this);   
    }
    
    @Override
    public Int intDivFrom(WordIntegerNumberImpl b) 
    { 
      return create(LargeInteger.valueOf(b.getValue()).divide(value)); 
    }
    
    @Override
    public Int intDivFrom(LargeIntegerNumberImpl b)
    { 
      return create(b.getValue().divide(value)); 
    } 
    
    @Override
    public String toString() {
      String str = value.toString();
      if ((value.compareTo(WordIntegerNumberImpl.MIN_VALUE)>=0) 
        && (value.compareTo(WordIntegerNumberImpl.MAX_VALUE)<=0))
          str = str + "REPORT ERROR: BigInteger has wordsize!";
      return  str;
    }
    
    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    LargeInteger asLargeInteger() {
      return this.value;
    }
}
