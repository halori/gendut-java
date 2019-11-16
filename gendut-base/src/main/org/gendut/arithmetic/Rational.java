package org.gendut.arithmetic;


//! Rational Numbers
/*<literate>*/
/**
It is discouraged to specialize this class outside from its package.
**/
public abstract class Rational {
	 
  protected Rational() { // users cannot instantiate this class
  }
       
  public static Int create(long value) {
    return LargeIntegerNumberImpl.create(value);
  }
  
   static Int create(LargeInteger value) {
    return LargeIntegerNumberImpl.create(value);
  }
  
  public static Rational create(long nom, long denom) { 
      return RationalNumberImpl.createFraction(create(nom),create(denom));
  }
  
  static Rational createFraction(LargeInteger nom, LargeInteger denom) {  
    return RationalNumberImpl.createFraction(create(nom),create(denom));
  }
  
    public abstract Rational add(Rational b);
    public abstract Rational multiply(Rational b);
    public abstract Rational subtract(Rational b);
    public abstract int compareTo(Rational b);
    public abstract Rational inverseZ();
    public abstract Rational inverse();
    
    public Rational divideZ(Rational b)
    {
    	return multiply(b.inverseZ());
    }
    
    public Rational divide(Rational b)
    {
      return multiply(b.inverseZ());
    }
    
    public Rational add(long b)
    {return add(Rational.create(b));  }
    public Rational multiply(long b)
    {return multiply(Rational.create(b));  }
    public Rational subtract(long b)
    {return subtract(Rational.create(b));  }
    public Rational divide(long b)
    {return divide(Rational.create(b));  }
    public int compareTo(long b)
    {return compareTo(Rational.create(b));  }
    
   
    
    public abstract boolean isInteger();
    public abstract Int Nominator();
    public abstract Int Denominator();
    
    /**
     * Test if number is zero.
     */
    final public boolean isZero() {
      /*
       * We test for identity since there are no public constructors
       * for rational numbers and the methods ensure there is only one
       * object with zero-value.
       */
      if (this == Int.ZERO) {
          return true;
      } else {
          return false;
      }
    }
    
    /**
     * returns -1/1 for negative/positive numbers and 0 for zero.
     */
    public int signum() {
    		int cmp = this.compareTo(Int.ZERO);
    		if (cmp > 0)
    			return 1;
    		else if (cmp == 0)
    			return 0;
    		else return -1;
    }
    
    public boolean equals(Rational b) {
        return  (this.compareTo(b) == 0);
    }
    
    @Override
    public boolean equals(Object b) {
      if (!(b instanceof Rational)) {
        return false;        
      } else
       return  (this.compareTo((Rational) b) == 0);
    }
    
  // implementation part: not meant as a stable user interface.
  // (mostly method dispatch to concrete specializations which
  // are subject to change!!!)
    
  // abstract definition of concrete binary operations:
    
    protected abstract Rational add(Int b);
    protected abstract Rational add(RationalNumberImpl b);
    protected abstract Rational multiply(Int b);
    protected abstract Rational multiply(RationalNumberImpl b);
    protected abstract Rational subtractFrom(Int b);
    protected abstract Rational subtractFrom(RationalNumberImpl b);
    protected abstract int compareTo(Int b);
    protected abstract int compareTo(RationalNumberImpl b);
}//`class`
