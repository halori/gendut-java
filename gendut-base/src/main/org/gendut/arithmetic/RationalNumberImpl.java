package org.gendut.arithmetic;


// ! Internal Representation
/* <literate> */
/**
 * Representation of Rationals
 */
final class RationalNumberImpl extends Rational {
  @Override
  public boolean isInteger() {
    return false;
  }

  private final Int nom, denom; // invariant: denominator is always > 0

  @Override
  public Int Nominator() {
    return nom;
  }

  @Override
  public Int Denominator() {
    return denom;
  }

  private RationalNumberImpl(Int nominator, Int denominator) {
    nom = nominator;
    denom = denominator;
  }

  public Int getNominator() {
    return nom;
  }

  public Int getDenominator() {
    return denom;
  }

  /**
   * The denominator must be non-zero
   */
  public static Rational createFraction(Int nom, Int denom) {
    if (nom.equals(Int.ZERO)) {
      return create(0);
    }

    int sgn = denom.signum();
    if (sgn == 0) {
      throw new IllegalArgumentException(
          "Zero denomiator exception");
    }

    if (denom.signum() == -1) {
      nom = Int.ZERO.subtract(nom);
      denom = Int.ZERO.subtract(denom);
    }

    Int gcd = nom.gcd(denom);

    if (!gcd.equals(Int.ONE)) {
      nom = nom.intDiv(gcd);
      denom = denom.intDiv(gcd);
    }
    if (denom.equals(Int.ONE)) {
      return nom;
    } else {
      return new RationalNumberImpl(nom, denom);
    }
  }

  // double dispatch for base class objects:
  @Override
  public Rational add(Rational b) {
    return b.add(this);
  }

  @Override
  public Rational multiply(Rational b) {
    return b.multiply(this);
  }

  @Override
  public Rational subtract(Rational b) {
    return b.subtractFrom(this);
  }

  @Override
  public int compareTo(Rational b) {
    return -b.compareTo(this);
  }

  // method implementation:

  @Override
  public Rational add(Int b) {
    return createFraction(b.multiply(denom).add(nom), denom);
  }

  @Override
  public Rational add(RationalNumberImpl b) {
    Int common_denom = b.getDenominator().multiply(denom);
    Int A = nom.multiply(b.getDenominator());
    Int B = b.getNominator().multiply(denom);
    return createFraction(A.add(B), common_denom);
  }

  @Override
  public Rational multiply(Int b) {
    return createFraction(b.multiply(nom), denom);
  }

  @Override
  public Rational multiply(RationalNumberImpl b) {
    return createFraction(b.getNominator().multiply(nom), b
        .getDenominator().multiply(denom));
  }

  @Override
  public Rational subtractFrom(Int b) {
    return createFraction((b.multiply(denom)).subtract(nom),
        denom);
  }

  @Override
  public Rational subtractFrom(RationalNumberImpl b) {
    Int common_denom = b.getDenominator().multiply(denom);
    Int A = nom.multiply(b.getDenominator());
    Int B = b.getNominator().multiply(denom);
    return createFraction(B.subtract(A), common_denom);
  }

  @Override
  public Rational inverseZ() {
    return createFraction(denom, nom);
  }

  @Override
  public Rational inverse() {
    if ((Rational) this == Int.ZERO) {
      throw new ArithmeticException("Division by Zero");
    }
    return createFraction(denom, nom);
  }

  @Override
  public int compareTo(Int b) {
    if ((Object) this == b) {
      return 0;
    }
    Int A = this.getNominator();
    Int C = this.getDenominator();
    // assert(C.compareTo(ZERO) >= 0);
    return A.compareTo(b.multiply(C));
  }

  @Override
  public int compareTo(RationalNumberImpl b) {
    if (this == b) {
      return 0;
    }
    Int A = this.getNominator();
    Int C = this.getDenominator();
    Int B = b.getNominator();
    Int D = b.getDenominator();
    // assert(C.compareTo(ZERO) >= 0);
    // assert(D.compareTo(ZERO) >= 0);
    return A.multiply(D).compareTo(B.multiply(C));
  }

  @Override
  public String toString() {
    return nom.toString() + "/" + denom.toString();
  }
  
  @Override
  public int hashCode() {
    return nom.hashCode() + denom.hashCode()*3173;
  }
}// class IRational
