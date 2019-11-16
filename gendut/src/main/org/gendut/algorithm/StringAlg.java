package org.gendut.algorithm;

//! Algorithms for Strings 
/*<literate>*/
/**
Algorithms for strings.
**/
final public class StringAlg {
  /**
   * This class has no instances.
   */
  private StringAlg() {
   
      }
  public static double lvdistance(String a, String b) {
    int m = a.length();
    int n = b.length();
    /*
     *  d is a table with $m+1$ rows and $n+1$ columns
     */
    int[][] d = new int[m+1][n+1];

    for (int i = 0; i <= m; i++)
      d[i][0] = i;
    for (int j = 0; j <= n; j++)
      d[0][j] = j;

    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        int cost;
        if (a.charAt(i-1) == b.charAt(j-1)) 
          cost = 0;
        else
          cost = 1;

        int deletionCost = d[i-1][j] + 1;
        int insertionCost = d[i][j-1] + 1;
        int substitutionCost =d[i-1][j-1] + cost;

        int min = deletionCost;
        if (insertionCost < min)
          min = insertionCost;
        if (substitutionCost < min)
          min = substitutionCost;

        d[i][j] = min;
      }//`for j`
    }//`for i`
  
    return d[m][n];
  }
}
