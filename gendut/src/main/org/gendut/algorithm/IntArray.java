package org.gendut.algorithm;

//!Routines for Integer Arrays
/*<literate>*/
public final class IntArray {
  private IntArray() {
  };

  /**
   * Merge two sorted arrays into one sorted array.
   */
  public static int[] mergeSortedArrays(int[] a, int[] b) {
    int N = a.length + b.length;
    int[] c = new int[N];
    int j = 0;
    int k = 0;
    for (int i = 0; i < N; i++) {
      if (j == a.length) {
        for (; i < N; i++)
          c[i] = b[k++];
      } else if (k == b.length) {
        for (; i < N; i++)
          c[i] = a[j++];
      } else if (a[j] <= b[k]) {
        c[i] = a[j++];
      } else {
        c[i] = b[k++];
      }// `else`
    }
    return c;
  }
  
  /**
   * Remove all elements of b from a.
   */
  public static int[] minusSortedArrays(int[] a, int[] b) {
    int[] c = new int[a.length];
    int i = 0;
    int j = 0;
    for(int k = 0; k < b.length; k++) {
       while(i < a.length && a[i] <= b[k]) {
           if(a[i] < b[k])
               c[j++] = a[i++];
           else
               i = i + 1;
       }
    }
    if (j == a.length)
        return c;
    int N = j+a.length-i;
    int[] d = new int[N];
    for(int k = 0; k < j; k++)
        d[k] = c[k];
    for(int k = j; k < N; k++)
        d[k] = a[k+i-j];
    return d;
  }
  
  public static int[] numbers(int n) {
      int[] c = new int[n];
      for (int i = 0; i < n; i++)
          c[i] = i;
      return c;
  }

  /**
   * Take an array of sorted integers and eliminates double entries.
   */
  public static int[] unifySortedArray(int[] a) {
    int N = a.length;
    if (N == 0)
      return a;

    /*
     * Count doubles:
     */
    int k = 0;
    for (int i = 1; i < N; i++) {
      if (a[i] == a[i - 1])
        k = k + 1;
    }

    /*
     * Produce array without doubles:
     */
    int M = N - k;
    int[] b = new int[M];
    b[0] = a[0];
    int j = 0;
    for (int i = 1; i < N; i++) {
      if (a[i] != b[j]) {
        j = j + 1;
        b[j] = a[i];
      }// `if`
    }// `for`
    return b;
  }

  static public String toString(int[] a) {
    StringBuffer str = new StringBuffer();
    str.append('{');
    for (int i = 0; i < a.length; i++) {
      if (i != 0)
        str.append(", ");
      str.append(Integer.toString(a[i]));
    }
    str.append('}');
    return str.toString();
  }

  public static boolean equals(int[] a, int[] b) {
    if (a == b)
      return true;
    if (a == null)
      return (b == null);
    else if (b == null)
      return false;

    if (a.length != b.length)
      return false;

    for (int i = 0; i < a.length; i++)
      if (a[i] != b[i])
        return false;

    return true;
  }

  public static boolean isContainedForSortedArrays(int[] a, int[] b) {
    int k = 0;
    for (int i = 0; i < a.length; i++) {
      if (k >= b.length)
        return false;
      while (b[k] < a[i]) {
        k = k + 1;
        if ((k >= b.length) || (b[k] > a[i]))
          return false;
      }
    }
    return true;
  }

  public static int[] differenceForSortedArrays(int[] a, int[] b) {
    int[] diff = new int[a.length + b.length];
    int n = 0;
    
    int j = 0;
    int k = 0;
    while(j < a.length && k < b.length) {
      if (a[j] < b[k]) {
        diff[n++] = a[j++];
      }
      else
      if (a[j] > b[k]) {
        diff[n++] = b[k++];
      }
      else {
        j = j + 1;
        k = k + 1;
      }
    }//`while`
    for(;j < a.length; j++)
      diff[n++] = a[j++];
    
    for(;k < b.length; j++)
      diff[n++] = b[k++];
    
    int[] result = new int[n];
    for (int i = 0; i<n; i++)
      result[i] = diff[i];
    return result;
  }
}// `class`
