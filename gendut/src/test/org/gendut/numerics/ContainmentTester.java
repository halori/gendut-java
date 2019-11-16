package org.gendut.numerics;

import org.gendut.func.Function;


//! Containment Tester 2D
/*<literate>*/
/**
 * This is a simple point containment tester for 2-dimensional polygons.
 */
public final class ContainmentTester implements Function<Vector, Boolean> {

  private final double[] px, py;
  private final double xmin, xmax, ymin, ymax;
  private final double e;

  /**
   * Each row of the matrix represents a point of the polygon. The second
   * argument denotes the tolerance: Points within distance <i>e</i> are
   * considered to be inside.
   */
  public ContainmentTester(Matrix polygon, double e) {

    if (e < 0.0) {
      throw new IllegalArgumentException("Error bound is negative.");
    }
    this.e = e;

    px = polygon.getColumn(0).rawData();
    py = polygon.getColumn(1).rawData();

    xmin = polygon.getColumn(0).min();
    xmax = polygon.getColumn(0).max();
    ymin = polygon.getColumn(1).min();
    ymax = polygon.getColumn(1).max();
  }

  /**
   * Test if a point is inside of the polygon.
   */
  public Boolean get(Vector v) {

    if (v.size() != 2)
      throw new IllegalArgumentException(
          "Expected a 2-dimensional vector.");

    double x = v.at(0);
    double y = v.at(1);
    /*
     * We test if $v$ is inside the bounding box.
     */
    if ((xmin <= x) && (x <= xmax) && (ymin <= y) && (y <= ymax))

    {
      boolean evenNumberOfIntersections = false;
      /*
       * We cast a horizontal line to the point and count the number of
       * intersections.
       */
      for (int i = 0, j = px.length - 1; i < px.length; j = i++) {
        if (
        /*
         * At least some part of the line segment must be left from the vertical
         * line at $x$.
         */
        (x <= px[i] || x <= px[j])
            &&
                    /*
         * The line segment must not be completely at either side of the
         * horizontal line at $y$.
         */
        (((py[i] <= y) && (y < py[j]))
            || ((py[j] <= y) && (y < py[i])))
            &&
                    /*
         * The line segment intersects the horizontal line at $y$ on the left
         * side from point $(x,y)$.
         */
        (((x < px[i]) && x < px[j]) || (x < (px[j] - px[i])
            * (y - py[i]) / (py[j] - py[i]) + px[i]))) {
          evenNumberOfIntersections = !evenNumberOfIntersections;
        }
      }
      if (evenNumberOfIntersections) {
        return true;
      }
    }

    if (e == 0.0) {
      /*
       * No tolerance given.
       */
      return false;
    }
    /*
     * Now check the distance to the polygon boundaries:
     */
    if ((xmin - e <= x) && (x <= xmax + e) && (ymin - e <= y)
        && (y <= ymax + e)) {
      for (int i = 0, j = px.length - 1; i < px.length; j = i++) {
        if (withinDistance(v, px[i], py[i], px[j], py[j], e)) {
          return true;
        }// `if`
      }
    }
    return false;
  }

  private static boolean withinDistance(Vector v, double qx1,
      double qy1, double qx2, double qy2, double e) {
    /*
     * Test bounding box:
     */
    double xmin = Math.min(qx1, qx2);
    double xmax = Math.max(qx1, qx2);
    double ymin = Math.min(qy1, qy2);
    double ymax = Math.max(qy1, qy2);
    double x = v.at(0);
    double y = v.at(1);
    if ((x < xmin - e) || (x > xmax + e) || (y < ymin - e)
        || (y > ymax + e)) {
      return false;
    }// `if`
    /*
     * Test distance to end points:
     */
    double eSquare = e * e;
    double d1Square =
        (x - qx1) * (x - qx1) + (y - qy1) * (y - qy1);
    if (d1Square <= eSquare) {
      return true;
    }
    double d2Square =
        (x - qx2) * (x - qx2) + (y - qy2) * (y - qy2);
    if (d2Square <= eSquare) {
      return true;
    }
    /*
     * All fast tests failed. Now project $(x,y)$ on the infinite extension of
     * line segment $q$, resulting in point $a$. If $v$ is on the line segment
     * and $|a-(x,y)|<e$ i.e. ($|a-(x,y)|^2 < e^2$), then $q$ is within
     * distance. Before, we translate $(x,y)$ and $q$ such that $q$ starts in
     * the origin.
     */
    x = x - qx1;
    y = y - qy1;
    double qx = qx2 - qx1;
    double qy = qy2 - qy1;
    double r = (x * qx) + (y * qy);
    double qLength = qx * qx + qy * qy;
    if (r < 0.0 || r > qLength) {
      return false;
    }
    r = r / qLength;
    double ax = r * qx;
    double ay = r * qy;
    double dx = ax - x;
    double dy = ay - y;
    return (dx * dx + dy * dy < eSquare);
  }
}
