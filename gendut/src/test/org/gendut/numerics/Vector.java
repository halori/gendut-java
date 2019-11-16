package org.gendut.numerics;

import java.util.Arrays;

public final class Vector {

  private final double[] data;

  public Vector(double[] data) {
    this.data = data.clone();
  }

  public Vector(double x, double y) {
    this.data = new double[] { x, y };
  }

  public Vector(double x, double y, double z) {
    this.data = new double[] { x, y, z };
  }

  public double[] rawData() {
    return data.clone();
  }

  public double at(int i) {
    return data[i];
  }

  public int size() {
    return data.length;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(data);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Vector other = (Vector) obj;
    if (!Arrays.equals(data, other.data))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return Arrays.toString(data);
  }

  Vector add(Vector b) {
    int n = data.length;
    if (b.data.length != n)
      throw new IllegalArgumentException("Adding vectors of incompatible size.");

    double[] sum = new double[n];
    for (int i = 0; i < n; i++)
      sum[i] = data[i] + b.data[i];
    return new Vector(sum);
  }

  public double min() {
    double result = data[0];
    for (int i = 1; i < data.length; i++) {
      if (data[i] < result)
        result = data[i];
    }
    return result;
  }

  public double max() {
    double result = data[0];
    for (int i = 1; i < data.length; i++) {
      if (data[i] > result)
        result = data[i];
    }
    return result;
  }
}
