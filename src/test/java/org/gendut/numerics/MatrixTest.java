package org.gendut.numerics;

import junit.framework.TestCase;

public class MatrixTest extends TestCase {
  public void testCreation() {
    Matrix A = new Matrix();
    assertEquals(0, A.rowCount());
    assertEquals(0, A.columnCount());

    A = A.appendRow(new double[] { 1.0, 2.0, 3.0, 4.0 });
    assertEquals(1, A.rowCount());
    assertEquals(4, A.columnCount());
    assertEquals("[[1.0, 2.0, 3.0, 4.0]]", A.toString());

    A = A.appendRow(new double[] { 5.0, 6.0, 7.0, 8.0 });
    assertEquals(2, A.rowCount());
    assertEquals(4, A.columnCount());
    assertEquals("[[1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 7.0, 8.0]]", A.toString());

    A = A.appendRow(new double[] { 1.0, 2.0, 3.0, 4.0 });
    assertEquals(3, A.rowCount());
    assertEquals(4, A.columnCount());
    assertEquals(
        "[[1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 7.0, 8.0], [1.0, 2.0, 3.0, 4.0]]",
        A.toString());

    A = A.appendRow(new double[] { 5.0, 6.0, 7.0, 8.0 });
    assertEquals(4, A.rowCount());
    assertEquals(4, A.columnCount());
    assertEquals(
        "[[1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 7.0, 8.0], [1.0, 2.0, 3.0, 4.0], [5.0, 6.0, 7.0, 8.0]]",
        A.toString());

    assertEquals("[1.0, 5.0, 1.0, 5.0]", A.getColumn(0).toString());
    assertEquals("[2.0, 6.0, 2.0, 6.0]", A.getColumn(1).toString());
    assertEquals("[3.0, 7.0, 3.0, 7.0]", A.getColumn(2).toString());
    assertEquals("[4.0, 8.0, 4.0, 8.0]", A.getColumn(3).toString());
  }

  public void testArithmetic() {
    Matrix A = new Matrix();
    A = A.appendRow(1.0, 0.0, 1.0);
    A = A.appendRow(1.0, 0.0, 2.0);
    A = A.appendRow(1.0, 1.0, 0.0);

    Matrix B = new Matrix();
    B = B.appendRow(2.0, 0.0, 1.0);
    B = B.appendRow(-1.0, 0.0, 2.0);
    B = B.appendRow(1.0, 1.0, 4.0);

    assertEquals("[[3.0, 0.0, 2.0], [0.0, 0.0, 4.0], [2.0, 2.0, 4.0]]", A
        .add(B).toString());
  }
}
