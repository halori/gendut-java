package org.gendut.numerics;

import junit.framework.TestCase;

public class VectorTest extends TestCase {

  public void testCreation() {
    Vector v = new Vector(new double[] { 1.0, 2.0, 3.0 });

    assertEquals(1.0, v.at(0));
    assertEquals(2.0, v.at(1));
    assertEquals(3.0, v.at(2));

    assertEquals("[1.0, 2.0, 3.0]", v.toString());

    assertEquals(3, v.size());
  }

  public void testArithmetic() {
    Vector a = new Vector(new double[] { 1.0, 2.0, 3.0 });
    Vector b = new Vector(new double[] { 2.0, -1.0, 3.0 });
    Vector c = a.add(b);
    assertEquals("[3.0, 1.0, 6.0]", c.toString());
    assertEquals(6.0, c.max());
    assertEquals(1.0, c.min());
  }
}
