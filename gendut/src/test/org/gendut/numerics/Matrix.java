package org.gendut.numerics;

public final class Matrix {

  private volatile Matrix rest;
  private volatile double[] data;
  private final int rowCnt;
  private final int colCnt;

  public int rowCount() {
    return rowCnt;
  }

  public int columnCount() {
    return colCnt;
  }

  void compactify() {
    if (rest == null)
      return;

    double[] rawData = new double[rowCount() * columnCount()];
    int k = 0;
    Matrix B = this;
    while (B != null) {
      for (int i = 0; i < B.data.length; i++)
        rawData[k++] = B.data[i];

      B = B.rest;
    }

    /*
     * The order of the following two assignments must remain: A thread must see
     * the new data field before rest is nullified.
     */
    data = rawData;
    rest = null;
  }

  public Matrix() {
    rest = null;
    data = null;
    rowCnt = 0;
    colCnt = 0;
  }

  public Matrix(double[] row) {
    rest = null;
    data = row.clone();
    rowCnt = 1;
    colCnt = row.length;
  }

  private Matrix(double[] data, int dataRowCount, Matrix B) {
    this.data = data;
    rowCnt = dataRowCount + (B == null ? 0 : B.rowCnt);
    if (data.length % dataRowCount != 0) {
      throw new IllegalArgumentException(
          "Size of data is not a multiple of the number of rows.");
    }
    colCnt = data.length / dataRowCount;
    rest = B;
  }

  private double internalAt(int row, int col) {
    Matrix m = this;
    int index = row * colCnt + col;
    while (index >= m.data.length) {
      index = index - m.data.length;
      m = m.rest;
    }
    return m.data[index];
  }

  private double at(int row, int col) {
    if (!(0 <= col && col < colCnt))
      throw new IndexOutOfBoundsException("Column index is " + col);

    if (!(0 <= row && row < rowCnt))
      throw new IndexOutOfBoundsException("Row index is " + row);

    return internalAt(rowCnt - row - 1, col);
  }

  public Matrix appendRow(double[] row) {
    if (rowCnt == 0)
      return new Matrix(row);

    if (row.length != colCnt)
      throw new IllegalArgumentException(
          "Row length doesn't match to column count.");

    /*
     * Determine how many rows need to be melted:
     */
    Matrix B = this;
    int rowsToMelt = 1;
    while (B != null && rowsToMelt >= B.data.length / colCnt) {
      rowsToMelt = rowsToMelt + B.data.length / colCnt;
      B = B.rest;
    }

    /*
     * Melt data from leading rows:
     */
    double[] newData = new double[colCnt * rowsToMelt];
    for (int i = 0; i < colCnt; i++)
      newData[i] = row[i];

    for (int k = 1; k < rowsToMelt; k++) {
      for (int i = 0; i < colCnt; i++) {
        newData[k * colCnt + i] = internalAt(k - 1, i);
      }
    }
    return new Matrix(newData, rowsToMelt, B);
  }

  public Matrix appendRow(Vector row) {
    return appendRow(row.rawData());
  }

  public Matrix appendRow(double r0, double r1) {
    return appendRow(new double[] { r0, r1 });
  }

  public Matrix appendRow(double r0, double r1, double r2) {
    return appendRow(new double[] { r0, r1, r2 });
  }

  public Matrix appendRow(double r0, double r1, double r2, double r3) {
    return appendRow(new double[] { r0, r1, r2, r3 });
  }

  @Override
  public String toString() {
    StringBuffer str = new StringBuffer();
    str.append("[");
    for (int k = 0; k < rowCount(); k++) {
      if (k > 0)
        str.append(", ");
      str.append("[");
      for (int i = 0; i < columnCount(); i++) {
        if (i > 0)
          str.append(", ");
        str.append(at(k, i));
      }
      str.append("]");
    }
    str.append("]");
    return str.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    long result = 1;
    for (int k = 0; k < rowCount(); k++)
      for (int i = 0; i < columnCount(); i++) {
        long v = Double.doubleToLongBits(internalAt(k, i));
        result = prime * result + (v ^ (v >>> 32));
      }

    return (int) result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Matrix other = (Matrix) obj;
    if (colCnt != other.colCnt)
      return false;
    if (rowCnt != other.rowCnt)
      return false;

    for (int k = 0; k < rowCount(); k++)
      for (int i = 0; i < columnCount(); i++)
        if (internalAt(k, i) != other.internalAt(k, i))
          return false;

    return true;
  }

  public Vector getColumn(int i) {
    if (!(0 <= i && i < colCnt))
      throw new IndexOutOfBoundsException("illegal column index " + i);
    this.compactify();
    double[] A = data;

    double[] colData = new double[rowCnt];
    for (int k = rowCnt - 1; k >= 0; k--) {
      colData[k] = A[i];
      i = i + colCnt;
    }

    return new Vector(colData);
  }

  Matrix add(Matrix M) {
    if ((rowCnt != M.rowCnt) || (colCnt != M.colCnt))
      throw new IllegalArgumentException("Matrices have incompatible size");

    this.compactify();
    M.compactify();

    double[] A = data;
    double[] B = M.data;
    int n = A.length;

    double[] sum = new double[n];

    for (int i = 0; i < A.length; i++)
      sum[i] = A[i] + B[i];

    return new Matrix(sum, rowCnt, null);
  }
}// `class`
